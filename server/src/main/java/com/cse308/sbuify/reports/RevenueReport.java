package com.cse308.sbuify.reports;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "revenue_report")
public class RevenueReport extends TableReport {
}
