package com.cse308.sbuify.playlist;

import com.cse308.sbuify.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class PlaylistFolder implements PlaylistComponent {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String name;

    /** Folder owner */
    @OneToOne
    @NotNull
    @JsonIgnore
    private User owner;

    /** Sort position */
    private Integer position;

    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public Boolean isFolder() {
        return true;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
