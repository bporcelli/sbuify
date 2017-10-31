package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
public class ArtistRequest {
    private Integer id;
    private LocalDateTime creation;
    private RecordLabel requestTo;
    private Artist requestFrom;
}
