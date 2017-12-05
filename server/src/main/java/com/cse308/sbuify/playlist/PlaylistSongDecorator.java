package com.cse308.sbuify.playlist;

import com.cse308.sbuify.common.api.DecoratorRegistry;
import com.cse308.sbuify.common.api.ResponseDecorator;
import com.cse308.sbuify.song.Song;

public class PlaylistSongDecorator implements ResponseDecorator<PlaylistSong> {

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(PlaylistSong.class);
    }

    @Override
    public void decorate(PlaylistSong original) {
        ResponseDecorator<Song> decorator = DecoratorRegistry.getDecorator(Song.class);
        if (decorator == null) {
            return;
        }
        decorator.decorate(original.getSong());
    }
}
