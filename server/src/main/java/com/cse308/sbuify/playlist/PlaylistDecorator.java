package com.cse308.sbuify.playlist;

import com.cse308.sbuify.common.api.ResponseDecorator;
import com.cse308.sbuify.customer.FollowedPlaylistRepository;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.user.User;

public class PlaylistDecorator implements ResponseDecorator<Playlist> {

    private AuthFacade authFacade;

    private FollowedPlaylistRepository followedPlaylistRepo;

    public PlaylistDecorator(AuthFacade authFacade, FollowedPlaylistRepository followedPlaylistRepo) {
        this.authFacade = authFacade;
        this.followedPlaylistRepo = followedPlaylistRepo;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(Playlist.class);
    }

    @Override
    public void decorate(Playlist playlist) {
        User user = authFacade.getCurrentUser();
        Boolean following = followedPlaylistRepo.existsByCustomerAndPlaylist_Id(user, playlist.getId());
        playlist.set("isFollowed", following);
    }
}
