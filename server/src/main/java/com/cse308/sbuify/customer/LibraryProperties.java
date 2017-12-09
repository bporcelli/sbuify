package com.cse308.sbuify.customer;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("library")
public class LibraryProperties {
    /**
     *  Item per page
     */
    private Integer itemPerPage = 25;

    public Integer getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(Integer itemPerPage) {
        this.itemPerPage = itemPerPage;
    }
}
