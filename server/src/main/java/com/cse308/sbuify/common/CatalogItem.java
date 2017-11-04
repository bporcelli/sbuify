package com.cse308.sbuify.common;

import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class CatalogItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty
    private String name;

    @NotNull
    private LocalDateTime dateCreation;

    @NotNull
    private Boolean active;

    @OneToOne
    @PrimaryKeyJoinColumn
    private User owner;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @PrimaryKeyJoinColumn
    private Image image;

    public CatalogItem() {
    }

    public CatalogItem(@NotEmpty String name, @NotNull LocalDateTime dateCreation, @NotNull Boolean active, @NotNull User owner, Image image) {
        this.name = name;
        this.dateCreation = dateCreation;
        this.active = active;
        this.owner = owner;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
