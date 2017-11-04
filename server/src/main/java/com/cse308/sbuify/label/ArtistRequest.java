package com.cse308.sbuify.label;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.label.RecordLabel;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class ArtistRequest implements Serializable{
    @Id
    private Integer id;
    private LocalDateTime creation;
    private RecordLabel requestTo;
    private Artist requestFrom;
}
