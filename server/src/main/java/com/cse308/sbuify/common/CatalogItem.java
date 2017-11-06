package com.cse308.sbuify.common;

import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.user.User;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class CatalogItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    private LocalDateTime createdDate;

    @NotNull
    private Boolean active = true;

    // todo: set cascade actions
    @OneToOne
    private User owner;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Image image;

    public CatalogItem() {}

    public CatalogItem(@NotEmpty String name, User owner, Image image) {
        this.name = name;
        this.owner = owner;
        this.image = image;
    }

    /**
     * Set date created when catalog item is first persisted.
     */
    @PrePersist
    private void onPrePersist() {
        this.createdDate = LocalDateTime.now();
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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
