package com.cse308.sbuify.playlist;

import com.cse308.sbuify.user.AppUser;

public interface PlaylistComponent {
    public String getName();
    public void setName(String name);
    public Integer getPosition();
    public void setPosition(Integer order);
    public AppUser getOwner();
    public void setOwner(AppUser owner);
    public PlaylistFolder getParentFolder();
    public void setParentFolder(PlaylistFolder folder);
    public Boolean isFolder();
}
