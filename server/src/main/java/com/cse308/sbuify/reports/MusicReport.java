package com.cse308.sbuify.reports;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "music_report")
public class MusicReport extends TableReport {
}
