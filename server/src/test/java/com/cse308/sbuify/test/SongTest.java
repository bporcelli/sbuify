package com.cse308.sbuify.test;

import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;
import com.cse308.sbuify.stream.StreamCountDTO;
import com.cse308.sbuify.stream.StreamRepository;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SongTest extends AuthenticatedTest {

    @Autowired
    private StreamRepository streamRepo;

    @Autowired
    private SongRepository songRepo;

    @Test
    @Transactional
    public void updatePlayCount() {
        LocalDateTime timeAfter = LocalDateTime.now().minusHours(6);
        List<StreamCountDTO> newStreams = streamRepo.getNewStreamsAfterTime(timeAfter);

        for (StreamCountDTO dto: newStreams) {
            Song song = dto.getSong();
            song.setPlayCount(song.getPlayCount() + dto.getNewStreams());
            songRepo.save(song);
        }
        // fixme: better test (this just checks for a crash)
    }

    @Override
    public String getEmail() {
        return "sbuify+admin@gmail.com";  // use the user sbuify+admin@gmail.com for all tests in this class request require admin role
    }

    @Override
    public String getPassword() {
        return "a";
    }
}
