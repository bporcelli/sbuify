package com.cse308.sbuify.customer;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Customer preference constants.
 */
@Entity
public class Preferences {
    public final static String HQ_STREAMING = "hq_streaming";
    public final static String LANGUAGE = "language";
    
    @Id
    private int id;
}
