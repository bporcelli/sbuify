package com.cse308.sbuify.common;

import com.cse308.sbuify.song.Song;

import java.util.Collection;

public interface Queueable {
    Collection<Song> getItems();
}
