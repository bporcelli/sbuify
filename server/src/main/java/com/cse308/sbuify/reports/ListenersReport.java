package com.cse308.sbuify.reports;

public class ListenersReport extends TableReport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ListenersReport() {
        super("listeners-report", "Monthly Listeners Report", ReportType.LABEL);
    }

    @Override
    public String getQuery() {
        return "select * from Stream where customer_id = ?1";
    }
  
    @Override
    public String getTemplatePath() {
        return "ListenerReport.tmpl";
    }
}
