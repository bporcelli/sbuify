package com.cse308.sbuify.customer;



import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
public class Preferences implements Serializable{
    @Id
    private Integer id;
    @Enumerated(EnumType.STRING)
    private Language selectedLanguage;

    @NotNull
    private Boolean isHdStreaming;

    @NotNull
    private Boolean isPrivateSession;

    @NotNull
    private Boolean showActivityFeed;

    @NotNull
    private Boolean isShuffling;

    @Enumerated(EnumType.STRING)
    private RepeatMode repeatMode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Language getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(Language selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public Boolean getHdStreaming() {
        return isHdStreaming;
    }

    public void setHdStreaming(Boolean hdStreaming) {
        isHdStreaming = hdStreaming;
    }

    public Boolean getPrivateSession() {
        return isPrivateSession;
    }

    public void setPrivateSession(Boolean privateSession) {
        isPrivateSession = privateSession;
    }

    public Boolean getShowActivityFeed() {
        return showActivityFeed;
    }

    public void setShowActivityFeed(Boolean showActivityFeed) {
        this.showActivityFeed = showActivityFeed;
    }

    public Boolean getShuffling() {
        return isShuffling;
    }

    public void setShuffling(Boolean shuffling) {
        isShuffling = shuffling;
    }

    public RepeatMode getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
    }


}
