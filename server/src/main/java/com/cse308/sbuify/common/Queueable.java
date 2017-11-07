package com.cse308.sbuify.common;

import java.util.Collection;

import com.cse308.sbuify.song.Song;

public interface Queueable {
    Collection<Song> getItems();
}
