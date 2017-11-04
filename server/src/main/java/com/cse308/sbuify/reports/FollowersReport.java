package com.cse308.sbuify.reports;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue(value = "followers_report")
public class FollowersReport extends TableReport {
}
