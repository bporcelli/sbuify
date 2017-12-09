package com.cse308.sbuify.common.api;

import com.cse308.sbuify.album.AlbumDecorator;
import com.cse308.sbuify.album.AlbumRepository;
import com.cse308.sbuify.artist.ArtistDecorator;
import com.cse308.sbuify.customer.FollowedPlaylistRepository;
import com.cse308.sbuify.customer.PlayQueueDecorator;
import com.cse308.sbuify.playlist.PlaylistDecorator;
import com.cse308.sbuify.playlist.PlaylistSongDecorator;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.song.SongDecorator;
import com.cse308.sbuify.song.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Response decorator registry.
 */
@Service
public class DecoratorRegistry {

    private final static List<ResponseDecorator> decorators = new ArrayList<>();

    @Autowired
    public DecoratorRegistry(
        AuthFacade authFacade,
        SongRepository songRepo,
        AlbumRepository albumRepo,
        FollowedPlaylistRepository followedPlRepo
    ) {
        decorators.add(new SongDecorator(songRepo, authFacade));
        decorators.add(new AlbumDecorator(albumRepo, authFacade));
        decorators.add(new ArtistDecorator(songRepo));
        decorators.add(new TypedCollectionDecorator());
        decorators.add(new PlaylistSongDecorator());
        decorators.add(new PlayQueueDecorator());
        decorators.add(new PlaylistDecorator(authFacade, followedPlRepo));
    }

    public static ResponseDecorator getDecorator(Class<?> type) {
        for (ResponseDecorator decorator: decorators) {
            if (decorator.supports(type)) {
                return decorator;
            }
        }
        return null;
    }
}
