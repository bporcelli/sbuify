package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class SavedSong {
    private PlayList playlist;
    private LocalDateTime added;
}
