package com.cse308.sbuify.playlist;

import com.cse308.sbuify.common.Queueable;
import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.common.api.DecorateResponse;
import com.cse308.sbuify.customer.*;
import com.cse308.sbuify.image.*;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.security.SecurityUtils;
import com.cse308.sbuify.song.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/api/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private FollowedPlaylistRepository followedPlaylistRepo;

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private StorageService storageService;

    @Autowired
    private PlaylistFolderRepository folderRepository;

    @Autowired
    private PlaylistSongRepository playlistSongRepo;

    private final Integer MAX_SONGS;

    private final Integer SONGS_PER_PAGE;

    @Autowired
    public PlaylistController(PlaylistProperties playlistProperties) {
        MAX_SONGS = playlistProperties.getMaxSongs();
        SONGS_PER_PAGE = playlistProperties.getSongsPerPage();
    }

    /**
     * Create a new playlist.
     * @param playlist The playlist to create.
     * @param folderId Optional folder ID. If provided, the playlist will be saved in this folder.
     * @return The playlist as saved in the database.
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createPlaylist(@RequestBody Playlist playlist, @RequestParam(required = false) Integer folderId) {
        Customer customer = (Customer) authFacade.getCurrentUser();

        // create playlist
        Image image = null;

        if (playlist.getImage() != null) {
            Base64Image rawImage = (Base64Image) playlist.getImage();
            try {
                image = storageService.save(rawImage.getDataURL());
            } catch (StorageException ex) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        playlist.setImage(image);
        playlist.setOwner(customer);

        Playlist saved = playlistRepository.save(playlist);

        // save the playlist in the user's library
        FollowedPlaylist followedPlaylist = new FollowedPlaylist(customer, saved);

        if (folderId != null) {
            Optional<PlaylistFolder> optionalFolder = folderRepository.findById(folderId);
            if (!optionalFolder.isPresent()) {
                return new ResponseEntity<>("Invalid folder ID.", HttpStatus.BAD_REQUEST);
            }
            followedPlaylist.setParent(optionalFolder.get());
        }

        followedPlaylistRepo.save(followedPlaylist);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Get information about a Playlist.
     * @param id The ID of the playlist.
     * @return The playlist if it exists and can be accessed by the authenticated user, otherwise void.
     */
    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @DecorateResponse(type = Playlist.class)
    public ResponseEntity<?> getPlaylistById(@PathVariable Integer id) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);

        if (!optionalPlaylist.isPresent()) { // playlist not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Playlist playlist = optionalPlaylist.get();

        if (!SecurityUtils.userCanEdit(playlist) && playlist.isHidden()) { // current user can't access playlist
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // if we reach this point, the user can access the playlist
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }

    /**
     * Get the songs in a playlist.
     * @param id Playlist ID.
     * @param page Page index.
     * @return a 200 response containing a list of PlaylistSong objects on success.
     */
    @GetMapping(path = "/{id}/songs")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @DecorateResponse(type = TypedCollection.class)
    public ResponseEntity<?> getPlaylistSongs(@PathVariable Integer id, @RequestParam(defaultValue = "0") Integer page) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);

        if (!optionalPlaylist.isPresent()) { // playlist not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Page<PlaylistSong> songs = playlistSongRepo.findAllByPlaylist_Id(id, PageRequest.of(page, SONGS_PER_PAGE));
        List<PlaylistSong> songList = new ArrayList<>();

        for (PlaylistSong song: songs) {
            songList.add(song);
        }
        return new ResponseEntity<>(new TypedCollection(songList, PlaylistSong.class), HttpStatus.OK);
    }

    /**
     * Update a playlist.
     * @param id The ID of the playlist to update.
     * @param updated Updated playlist.
     * @return a 200 response containing the updated playlist, otherwise a 404 if the playlist ID is invalid
     *         or a 403 if the current user does not have permission to update the playlist.
     */
    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> updatePlaylist(@PathVariable Integer id, @RequestBody Playlist updated) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);

        if (!optionalPlaylist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Playlist playlist = optionalPlaylist.get();

        if (!SecurityUtils.userCanEdit(playlist)) {  // can't edit playlist
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (updated.getName() != null) {
            playlist.setName(updated.getName());
        }
        if (updated.getDescription() != null) {
            playlist.setDescription(updated.getDescription());
        }
        if (updated.isHidden() != null) {
            playlist.setHidden(updated.isHidden());
        }

        ImageI newImage = updated.getImage();

        if (!(newImage instanceof Image)) {  // image was edited
            if (newImage instanceof Base64Image) {
                try{
                    newImage = storageService.save(((Base64Image) newImage).getDataURL());
                } catch (StorageException ex){
                    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
                }
            }

            if (playlist.getImage() != null) {
                Image oldImage = (Image) playlist.getImage();
                this.storageService.delete(oldImage);
            }
            playlist.setImage(newImage);
        }

        playlist = playlistRepository.save(playlist);
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }

    /**
     * Delete a playlist.
     * @param id The ID of the saved playlist to delete.
     * @return a 200 response if the operation is successful, a 404 if the playlist ID is invalid, or a
     *         403 if the current user does not have permission to delete the playlist.
     */
    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Transactional
    public ResponseEntity<?> deletePlaylist(@PathVariable Integer id) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);

        if (!optionalPlaylist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Playlist playlist = optionalPlaylist.get();

        if (!SecurityUtils.userCanEdit(playlist)) {  // can't delete playlist
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (playlist.getImage() != null) {
            Image image = (Image) playlist.getImage();
            this.storageService.delete(image);
        }
        followedPlaylistRepo.deleteAllByPlaylist(playlist);
        playlistRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Add a song or album to a playlist.
     * @param id Playlist ID.
     * @param toAdd The song or album to add.
     * @return a 200 response with the number of songs added in body on success, a 404 if the playlist ID is
     *         invalid, and a 403 otherwise.
     */
    @PostMapping(path = "/{id}/add")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> addToPlaylist(@PathVariable Integer id, @RequestBody Queueable toAdd) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);

        if (!optionalPlaylist.isPresent()) {
            return new ResponseEntity<>("Invalid playlist ID.", HttpStatus.NOT_FOUND);
        }
        Playlist playlist = optionalPlaylist.get();

        if (!SecurityUtils.userCanEdit(playlist)) { // can't edit playlist
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Collection<Song> newSongs = toAdd.getItems();

        int numSongs = playlistSongRepo.countAllByPlaylist(playlist);

        if (numSongs + newSongs.size() > MAX_SONGS) {
            return new ResponseEntity<>("Maximum playlist size exceeded.", HttpStatus.BAD_REQUEST);
        }

        List<PlaylistSong> newSongsList = newSongs.stream()
                .map(song -> new PlaylistSong(playlist, song))
                .collect(Collectors.toList());

        playlistSongRepo.saveAll(newSongsList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Remove a song or album from a playlist.
     * @param id The ID of the playlist.
     * @param toDelete The song or album to remove.
     * @return a 200 response if the operation succeeds, a 404 if the playlist ID is invalid, a 400 if the
     *         provided song list is invalid, or a 403 otherwise.
     */
    @PostMapping(path = "/{id}/remove")
    @Transactional
    public ResponseEntity<?> removeFromPlaylist(@PathVariable Integer id, @RequestBody Queueable toDelete) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);

        if (!optionalPlaylist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Playlist playlist = optionalPlaylist.get();

        Collection<Song> songs = toDelete.getItems();

        int numSongs = playlistSongRepo.countAllByPlaylist(playlist);

        if (numSongs - songs.size() < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!SecurityUtils.userCanEdit(playlist)) {  // can't edit playlist
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        for (Song song: songs) {
            playlistSongRepo.deleteByPlaylistAndSong(playlist, song);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
