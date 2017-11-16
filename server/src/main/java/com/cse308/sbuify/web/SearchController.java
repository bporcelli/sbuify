package com.cse308.sbuify.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.album.AlbumRepository;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.label.RecordLabel;
import com.cse308.sbuify.label.RecordLabelRepository;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.playlist.PlaylistRepository;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;

@Controller
@RequestMapping(path = "/api/search")
public class SearchController {

    @Autowired
    private SongRepository songRepo;

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private AlbumRepository albumRepo;

    @Autowired
    private PlaylistRepository playlistRepo;

    @Autowired
    private RecordLabelRepository labelRepo;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(path = "/songs")
    public @ResponseBody Iterable<Song> searchSongs(@RequestParam String keyword) {
        logger.debug("Queried keyword: " + keyword);
        Iterable<Song> searchResult = songRepo.findByKeyword(keyword);

        return searchResult;
    }

    @GetMapping(path = "/artists")
    public @ResponseBody Iterable<Artist> searchArtists(@RequestParam String keyword,
            @RequestParam(defaultValue = "false") Boolean unmanaged) {
        logger.debug("Queried keyword: " + keyword);

        Iterable<Artist> searchResult;

        if (unmanaged) {
            searchResult = artistRepo.findByKeywordUnmanaged(keyword);

        } else {
            searchResult = artistRepo.findByKeyword(keyword);

        }

        return searchResult;
    }

    @GetMapping(path = "/albums")
    public @ResponseBody Iterable<Album> searchAlbums(@RequestParam String keyword) {
        logger.debug("Queried keyword: " + keyword);

        Iterable<Album> searchResult = albumRepo.findByKeyword(keyword);

        return searchResult;
    }

    @GetMapping(path = "/playlists")
    public @ResponseBody Iterable<Playlist> searchPlaylists(@RequestParam String keyword) {
        logger.debug("Queried keyword: " + keyword);
        Iterable<Playlist> searchResult = playlistRepo.findByKeyword(keyword);

        return searchResult;
    }

    @GetMapping(path = "/labels")
    public @ResponseBody Iterable<RecordLabel> searchLabel(@RequestParam String keyword) {
        logger.debug("Queried keyword: " + keyword);
        Iterable<RecordLabel> searchResult = labelRepo.findByKeyword(keyword);

        return searchResult;
    }
}
