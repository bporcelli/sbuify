package com.cse308.sbuify.reports;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("report")
public class ReportProperties {
    /**
     * Location for storing report template files.
     */
    private String directory = "static/report_template";

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
