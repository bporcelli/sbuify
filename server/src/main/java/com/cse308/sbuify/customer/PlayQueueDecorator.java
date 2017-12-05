package com.cse308.sbuify.customer;

import com.cse308.sbuify.common.api.DecoratorRegistry;
import com.cse308.sbuify.common.api.ResponseDecorator;
import com.cse308.sbuify.song.Song;

public class PlayQueueDecorator implements ResponseDecorator<PlayQueue> {
    @Override
    public boolean supports(Class<?> type) {
        return type.equals(PlayQueue.class);
    }

    @Override
    public void decorate(PlayQueue original) {
        ResponseDecorator<Song> decorator = DecoratorRegistry.getDecorator(Song.class);
        if (decorator == null) {
            return;
        }
        for (Song song: original.getSongs()) {
            decorator.decorate(song);
        }
    }
}
