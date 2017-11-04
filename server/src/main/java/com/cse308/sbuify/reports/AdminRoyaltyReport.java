package com.cse308.sbuify.reports;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;



@Entity
@DiscriminatorValue(value = "admin_royalty_report")
public class AdminRoyaltyReport extends TableReport {

}
