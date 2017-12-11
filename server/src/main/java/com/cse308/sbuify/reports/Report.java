package com.cse308.sbuify.reports;

import java.io.Serializable;
import java.util.Map;

public abstract class Report implements Serializable {
    // ID of report, e.g. "royalty-report"
    private String id;

    // Name of report, e.g. "Royalty Report"
    private String name;

    // Type of report
    private ReportType type;

    public Report(String id, String name, ReportType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    /**
     * Returns the query used to generate this report.
     * @return String
     */
    public abstract String getQuery();

    /**
     * Returns the path to the report template.
     * @return String
     */
    public abstract String getTemplatePath();
    
    /**
     * Returns a key value pair to replace in template
     * 
     */
    public abstract Map<String, String> getReplaceMap();

    /**
     * Getters and setters.
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }
}
