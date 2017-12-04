package com.cse308.sbuify.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.image.StorageService;
import com.cse308.sbuify.playlist.PlaylistFolder;
import com.cse308.sbuify.playlist.PlaylistFolderRepository;
import com.cse308.sbuify.song.SongRepository;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import com.cse308.sbuify.user.UserRepository;

public class PlaylistFolderControllerTest extends AuthenticatedTest {

    private static final String TEST_IMAGE = "test-image.jpg";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private PlaylistFolderRepository playlistFolderRepository;

    /**
     * Test to get playlist by ID
     */
    @Test
    public void createPlaylistFolder() {
        Customer customer = (Customer) user;

        List<PlaylistFolder> playlistFolders = playlistFolderRepository.findAllByOwner_Id(customer.getId());
        int previousSize = playlistFolders.size();

        PlaylistFolder reqObj = new PlaylistFolder();
        reqObj.setName("New Playlist Folder");
        reqObj.setOwner(customer);
        reqObj.setParentFolder(null);
        reqObj.setPosition(previousSize + 1);

        HttpEntity<PlaylistFolder> request = new HttpEntity<>(reqObj);
        ResponseEntity<PlaylistFolder> response = restTemplate.postForEntity("/api/playlist-folders", request,
                PlaylistFolder.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        playlistFolders = playlistFolderRepository.findAllByOwner_Id(customer.getId());
        assertEquals(playlistFolders.size(), previousSize + 1);
    }

    @Test
    public void updatePlaylistFolder() {
        Customer customer = (Customer) user;

        List<PlaylistFolder> playlistFolders = playlistFolderRepository.findAllByOwner_Id(customer.getId());
        PlaylistFolder reqObj = playlistFolders.get(0);

        String newName = "Modified PlaylistFolder Name";
        Integer newPosition = 1;
        // parent folder is not tested because it is likely that it will be removed

        reqObj.setName(newName);
        reqObj.setPosition(newPosition);

        HttpEntity<PlaylistFolder> request = new HttpEntity<>(reqObj);
        ResponseEntity<Void> response = restTemplate.exchange("/api/playlist-folders/" + reqObj.getId(), HttpMethod.PATCH, request,
                Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        playlistFolders = playlistFolderRepository.findAllByOwner_Id(customer.getId());
        PlaylistFolder updated = playlistFolders.get(0);

        assertEquals(newName, updated.getName());
        assertEquals(newPosition, updated.getPosition());
    }

    @Override
    public String getEmail() {
        return "sbuify+b@gmail.com";
    }

    @Override
    public String getPassword() {
        return "b";
    }
}
