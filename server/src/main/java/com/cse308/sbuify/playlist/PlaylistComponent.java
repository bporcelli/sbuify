package com.cse308.sbuify.playlist;

import com.cse308.sbuify.user.User;

public interface PlaylistComponent {
    String getName();
    void setName(String name);
    User getOwner();
    void setOwner(User owner);
    Boolean isFolder();
}
