package com.cse308.sbuify.song;

import com.cse308.sbuify.common.api.ResponseDecorator;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.user.User;

/**
 * A service that decorates songs with additional user-specific metadata.
 */
public class SongDecorator implements ResponseDecorator<Song> {

    private SongRepository songRepository;

    private AuthFacade authFacade;

    public SongDecorator(SongRepository songRepository, AuthFacade authFacade) {
        this.songRepository = songRepository;
        this.authFacade = authFacade;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(Song.class);
    }

    @Override
    public void decorate(Song song) {
        User user = authFacade.getCurrentUser();

        Boolean isQueued = songRepository.isQueuedByUser(song.getId(), user.getId()) == 1;
        Boolean isSaved = songRepository.isSavedByUser(song.getId(), user.getId()) == 1;

        song.set("queued", isQueued);
        song.set("saved", isSaved);
    }
}
