package com.cse308.sbuify.reports;

public abstract class TableReport extends Report {
    // todo: set
    private static final String TABLE_TEMPLATE = "";

    public TableReport(String id, String name, ReportType type) {
        super(id, name, type);
    }

    /**
     * By default, use TABLE_TEMPLATE to render table reports.
     * @return String
     */
    @Override
    public String getTemplate() {
        return TABLE_TEMPLATE;
    }
}