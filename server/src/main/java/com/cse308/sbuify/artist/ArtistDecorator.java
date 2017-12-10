package com.cse308.sbuify.artist;

import com.cse308.sbuify.common.api.DecoratorRegistry;
import com.cse308.sbuify.common.api.ResponseDecorator;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.customer.FollowedArtistRepository;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;
import com.cse308.sbuify.user.User;

import java.util.List;

public class ArtistDecorator implements ResponseDecorator<Artist> {

    private AuthFacade authFacade;

    private SongRepository songRepository;

    private FollowedArtistRepository followedArtistRepo;

    public ArtistDecorator(
        SongRepository songRepository,
        FollowedArtistRepository followedArtistRepo,
        AuthFacade authFacade
    ) {
        this.songRepository = songRepository;
        this.followedArtistRepo = followedArtistRepo;
        this.authFacade = authFacade;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(Artist.class);
    }

    @Override
    public void decorate(Artist artist) {
        User user = authFacade.getCurrentUser();

        // add popular songs
        List<Song> popularSongs = songRepository.getPopularByArtist(artist.getId());

        // fixme: come up with a better way to avoid infinite recursion during serialization
        Artist artistClone;
        try {
            artistClone = (Artist) artist.clone();
        } catch (CloneNotSupportedException e) {
            return;
        }

        ResponseDecorator<Song> decorator = DecoratorRegistry.getDecorator(Song.class);

        for (Song song: popularSongs) {
            decorator.decorate(song);
            song.getAlbum().setArtist(artistClone);
        }

        artist.set("popularSongs", popularSongs);

        if (!(user instanceof Customer)) {  // done decorating
            return;
        }

        // add followed flag
        Boolean followed = followedArtistRepo.existsByCustomerAndArtist_Id(user, artist.getId());
        artist.set("followed", followed);

        // number of followers
        Integer followerCount = followedArtistRepo.countByCustomerAndArtist(user, artist);
        artist.set("numFollowers", followerCount);
    }
}
