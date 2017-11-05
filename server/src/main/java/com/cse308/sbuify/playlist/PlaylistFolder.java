package com.cse308.sbuify.playlist;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PlaylistFolder implements PlaylistComponent {
    @Id
    private Integer id;
    @Override
    public void setParent(PlaylistFolder folder) {

    }
}
