package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
