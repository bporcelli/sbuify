package com.cse308.sbuify.reports;

import com.cse308.sbuify.admin.Admin;
import com.cse308.sbuify.customer.Customer;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "table_report")
public class TableReport extends Report{

    @NotEmpty
    private String TABLE_TEMPLATE;
}
