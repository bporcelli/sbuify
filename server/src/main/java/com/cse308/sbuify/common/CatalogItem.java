package com.cse308.sbuify.common;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@MappedSuperclass
public abstract class CatalogItem implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    private LocalDateTime createdDate;

    @NotNull
    private Boolean active = true;

    // TODO: set cascade actions

    @JsonIgnore
    @OneToOne
    private User owner;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Image image;

    public CatalogItem() {
    }

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
    

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = "";
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }
    
    @Override
    public boolean equals(Object that) {
        if(that == null || !(that instanceof CatalogItem))
            return false;
        
        final CatalogItem thatCI = (CatalogItem) that;
        
        if(this.getId() == null ? thatCI.getId() != null : !this.getId().equals(thatCI.getId()))
            return false;
        if(this.getName() == null ? thatCI.getName() != null : !this.getName().equals(thatCI.getName()))
            return false;
        if(this.getCreatedDate() == null ? thatCI.getCreatedDate() != null : !this.getCreatedDate().equals(thatCI.getCreatedDate()))
            return false;
        if(this.isActive() == null ? thatCI.isActive() != null : !this.isActive().equals(thatCI.isActive()))
            return false;
//        if(this.getOwner() == null ? thatCI.getOwner() != null : !this.getOwner().equals(thatCI.getOwner()))
//            return false;
//        if(this.getImage() == null ? thatCI.getImage() != null : !this.getImage().equals(thatCI.getImage()))
//            return false;
        
        return true;
    }
}
