package com.cse308.sbuify.security;

import com.cse308.sbuify.admin.Admin;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.label.Label;
import com.cse308.sbuify.label.LabelOwner;
import com.cse308.sbuify.playlist.PlaylistFolder;
import com.cse308.sbuify.user.User;
import org.apache.http.auth.AUTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityUtils {

    private static AuthFacade authFacade;

    @Autowired
    public SecurityUtils(AuthFacade authFacade) {
        SecurityUtils.authFacade = authFacade;
    }

    /**
     * Convert the given user's granted authorities to a list of strings.
     * @param user
     * @return a list of strings describing the user's granted authorities.
     */
	public static List<String> getAuthorityStrings(UserDetails user) {
        ArrayList<String> scopes = new ArrayList<>();

        for (GrantedAuthority auth: user.getAuthorities()) {
            scopes.add(auth.toString());
        }

        return scopes;
    }

    /**
     * Determine whether the current user can edit the given catalog item.
     */
    public static boolean userCanEdit(CatalogItem item) {
        User user = authFacade.getCurrentUser();
        return item.getOwner().equals(user) || user instanceof Admin;
    }

    /**
     * Determine whether the current user can edit a playlist folder.
     */
    public static boolean userCanEdit(PlaylistFolder folder) {
        User user = authFacade.getCurrentUser();
        return folder.getOwner().equals(user);
    }

    /**
     * Determine whether the current user can edit a user.
     */
    public static boolean userCanEdit(User user) {
        User currentUser = authFacade.getCurrentUser();
        return currentUser.equals(user) || currentUser instanceof Admin;
    }

    /**
     * Determine whether the current user can edit an artist.
     */
    public static boolean userCanEdit(Artist artist) {
        User user = authFacade.getCurrentUser();
        boolean userOwnsArtist = artist.getOwner() != null && artist.getOwner().getId().equals(user.getId());
        return userOwnsArtist || user instanceof Admin;
    }

    /**
     * Determine whether the current user can edit a label.
     */
    public static boolean userCanEdit(Label label) {
        User user = authFacade.getCurrentUser();
        boolean owner = label.getOwner() != null && label.getOwner().equals(user);
        return owner || (user instanceof Admin);
    }
}
