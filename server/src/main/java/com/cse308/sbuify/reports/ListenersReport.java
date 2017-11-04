package com.cse308.sbuify.reports;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue(value = "listeners_report")
public class ListenersReport extends TableReport {
}
