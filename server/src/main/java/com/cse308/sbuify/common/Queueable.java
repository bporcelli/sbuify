package com.cse308.sbuify.common;

import java.util.Collection;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.song.Song;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property="type")
@JsonSubTypes({ @JsonSubTypes.Type(value = Album.class, name = "album"),
        @JsonSubTypes.Type(value = Song.class, name = "song") })
public interface Queueable {
    Collection<Song> getItems();
}
