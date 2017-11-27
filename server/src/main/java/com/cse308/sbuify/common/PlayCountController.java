package com.cse308.sbuify.common;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cse308.sbuify.song.SongRepository;
import com.cse308.sbuify.stream.StreamCountDTO;
import com.cse308.sbuify.stream.StreamRepository;

@Controller
@RequestMapping(path = "/api/cron")
public class PlayCountController {

    @Autowired
    private SongRepository songRepo;

    @Autowired
    private StreamRepository streamRepo;

    /**
     * Update the play counts for all songs.
     */
    @GetMapping(path = "/update-play-counts")
    @PreAuthorize("hasAnyRole('CRON')")
    public ResponseEntity<?> updatePlayCounts() {

        LocalDateTime start = getStartTime();
        LocalDateTime end = getEndTime();

        List<StreamCountDTO> dtos = streamRepo.findPlayCounts(start, end);

        while (!dtos.isEmpty()) {
            StreamCountDTO dto = dtos.remove(0);

            int songId = dto.getSongId();

            int newStreams = dto.getNewStreams();

            songRepo.incrementPlayCountById(songId, newStreams);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private LocalDateTime getStartTime() {
        // TODO Auto-generated method stub
        return null;
    }

    private LocalDateTime getEndTime() {
        // TODO Auto-generated method stub
        return null;
    }
}
