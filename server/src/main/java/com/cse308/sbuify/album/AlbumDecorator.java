package com.cse308.sbuify.album;

import com.cse308.sbuify.common.api.DecoratorRegistry;
import com.cse308.sbuify.common.api.ResponseDecorator;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.user.User;

public class AlbumDecorator implements ResponseDecorator<Album> {

    private AlbumRepository albumRepository;

    private AuthFacade authFacade;

    public AlbumDecorator(AlbumRepository albumRepository, AuthFacade authFacade) {
        this.albumRepository = albumRepository;
        this.authFacade = authFacade;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(Album.class);
    }

    @Override
    public void decorate(Album album) {
        // decorate the album...
        User user = authFacade.getCurrentUser();
        Boolean isSaved = albumRepository.isSavedByUser(user.getId(), album.getId()) == 1;
        album.set("saved", isSaved);

        // ... and all of its songs
        ResponseDecorator<Song> decorator = DecoratorRegistry.getDecorator(Song.class);
        if (decorator == null) {
            return;
        }
        for (Song song: album.getSongs()) {
            decorator.decorate(song);
        }
    }
}
