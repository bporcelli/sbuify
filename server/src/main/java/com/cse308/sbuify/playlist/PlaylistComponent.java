package com.cse308.sbuify.playlist;

import com.cse308.sbuify.user.User;

public interface PlaylistComponent {
    public String getName();
    public void setName(String name);
    public Integer getPosition();
    public void setPosition(Integer order);
    public User getOwner();
    public void setOwner(User owner);
    public PlaylistFolder getParentFolder();
    public void setParentFolder(PlaylistFolder folder);
    public Boolean isFolder();
}
