package com.cse308.sbuify.reports;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReportRegistry {

    // Registered reports
    private List<Report> reports = new ArrayList<>();

    /**
     * Constructor. Initializes reports.
     */
    public ReportRegistry() {
        this.register(new AdminRoyaltyReport());
        this.register(new ArtistRoyalyReport());
        this.register(new FollowersReport());
        this.register(new ListenersReport());
        this.register(new MusicReport());
        this.register(new RevenueReport());
        this.register(new SubscriberReport());
    }

    /**
     * Register a report.
     * @param report
     */
    public void register(Report report) {
        this.reports.add(report);
    }

    /**
     * Get a report by ID.
     * @param id
     * @return report with given ID, or null if no such report exists.
     */
    public Report getReport(String id) {
        for (Report report: this.reports) {
            if (id.equals(report.getId())) {
                return report;
            }
        }

        return null;
    }

    /**
     * Get all reports of a particular type.
     *
     * @param type
     * @return a list containing all reports of the specified type.
     */
    public List<Report> getReports(ReportType type) {
        List<Report> foundReports = new ArrayList<>();

        for (Report report: this.reports) {
            if (type == report.getType()) {
                foundReports.add(report);
            }
        }

        return foundReports;
    }

}