package com.cse308.sbuify.test;

import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;
import com.cse308.sbuify.stream.Stream;
import com.cse308.sbuify.stream.StreamRepository;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import net.bytebuddy.asm.Advice;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class SongTest extends AuthenticatedTest {

    @Autowired
    private StreamRepository streamRepo;

    @Autowired
    private SongRepository songRepo;

    @Test
    public void updatePlayCount(){
        LocalDateTime timeAfterNthHr = LocalDateTime.now().minusHours(6);
        List<Stream> streamsAfter = streamRepo.getAllByTimeAfter(timeAfterNthHr);
        Map<Integer,Integer> map = new Hashtable<>();

        for(Stream stream: streamsAfter){
            Integer songId = stream.getSong().getId();
            Integer streamCount = map.get(songId);
            if (streamCount == null){
                streamCount = 1;
            } else {
                streamCount++;
            }
            map.put(songId, streamCount);
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            Song song = getSongById(key);
            if (song == null){
                continue;
            }
            song.setPlayCount(song.getPlayCount() + value);
            songRepo.save(song);
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            Song song = getSongById(key);
            if (song == null){
                continue;
            }
            assertEquals(value, song.getPlayCount());
        }
    }

    private Song getSongById(Integer songId){
        Optional<Song> songOptional = songRepo.findById(songId);
        if(!songOptional.isPresent()){
            return null;
        }
        return songOptional.get();
    }

    @Test
    public void testInitialized() {}

    @Override
    public String getEmail() {
        return "sbuify+admin@gmail.com";  // use the user sbuify+admin@gmail.com for all tests in this class request require admin role
    }

    @Override
    public String getPassword() {
        return "a";
    }
}
