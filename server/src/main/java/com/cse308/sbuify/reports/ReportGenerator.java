package com.cse308.sbuify.reports;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;


/**
 * Report Generator.
 *
 * Responsible for generating & saving reports.
 */ 
public class ReportGenerator {

    public String generateHTML(EntityManager em, Report report, List<String> qparams) {
        String[] params = {};
        List<Object[]> results = getData(em, report, qparams.toArray(params));
        String templatePath = report.getTemplatePath();
        return SBUifyTemplateEngine.process(ReportController.DIRECTORY + templatePath, results);
    }

    public List<Object[]> getData(EntityManager em, Report report, String[] params) {
        Query query = buildQuery(em, report);
        
        int pos = 1;
        for(String s: params) {
            query.setParameter(pos++ , s);
        }
        List<Object[]> results = query.getResultList();
        return results;
    }

    private Query buildQuery(EntityManager em, Report report) {
        String qString = report.getQuery();
        return em.createNativeQuery(qString);
    }
}
