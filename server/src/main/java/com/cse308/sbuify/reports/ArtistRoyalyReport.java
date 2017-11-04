package com.cse308.sbuify.reports;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue(value = "artist_royalty_report")
public class ArtistRoyalyReport extends TableReport {
}
