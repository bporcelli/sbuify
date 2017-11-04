package com.cse308.sbuify.playlist;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.cse308.sbuify.playlist.PlayList;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class SavedSong implements Serializable {
    @Id
    private Integer id;
    private PlayList playlist;
    private LocalDateTime added;
}
