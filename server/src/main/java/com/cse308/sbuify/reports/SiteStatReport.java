package com.cse308.sbuify.reports;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SiteStatReport extends TableReport{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String CREATED_DATE_KEY = "%CREATED_DATE%";

    public SiteStatReport() {
        super("site-stat-report", "Site Statistics Report", ReportType.ADMIN);
    }
    
    @Override
    public String getQuery() {
        return "SELECT" + 
                "(SELECT COUNT(*) FROM User U WHERE U.user_type = 'Customer' ) AS CustomerCount," + 
                "(SELECT COUNT(*) FROM User U WHERE U.user_type = 'Admin') AS AdminCount," + 
                "(SELECT COUNT(*) FROM Song) AS SongCount," + 
                "(SELECT COUNT(*) FROM Playlist) AS PlaylistCount" + 
                ";";
    }

    @Override
    public String getTemplatePath() {
        return "SiteStatReport.tmpl";
    }
    
    @Override
    public Map<String, String> getReplaceMap(){
        Map<String, String> replaceMap = new HashMap<>();
        replaceMap.put(CREATED_DATE_KEY, LocalDateTime.now().toString());
        return replaceMap;
    }
}
