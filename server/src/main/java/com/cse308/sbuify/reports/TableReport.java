package com.cse308.sbuify.reports;

import java.util.HashMap;
import java.util.Map;

public abstract class TableReport extends Report {
    // todo: set

    public TableReport(String id, String name, ReportType type) {
        super(id, name, type);
    }

    /**
     * By default, use TABLE_TEMPLATE to render table reports.
     * @return String
     */
    @Override
    public String getTemplatePath() {
        return "TableReport.tmpl";
    }
    
    /**
     * By default, return empty replace map.
     * @return Map<String, String>: Empty replacement map
     */
    @Override
    public Map<String, String> getReplaceMap(){
        return new HashMap<>();
    }
}
