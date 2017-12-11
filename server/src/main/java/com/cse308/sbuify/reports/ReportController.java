package com.cse308.sbuify.reports;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cse308.sbuify.admin.Admin;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.label.LabelOwner;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.user.User;

/**
 * Report Generator.
 *
 * Responsible for generating & saving reports.
 */
@Controller
@RequestMapping(path = "/api/reports")
public class ReportController {

    @Autowired
    private EntityManager em;

    @Autowired
    private AuthFacade authFacade;
    
    @Autowired
    private ArtistRepository artistRepo;

    private ReportRegistry registry = new ReportRegistry();
    
    static String DIRECTORY;
    
    @Autowired
    public ReportController(ReportProperties reportProperties) {
        DIRECTORY = reportProperties.getDirectory();
    }

    /**
     * Endpoint for returning all of the reports available for the current user
     * 
     * @return
     */
    @GetMapping
    public @ResponseBody TypedCollection getReports() {
        User user = authFacade.getCurrentUser();

        List<Report> reports;
        if (user instanceof LabelOwner) {
            reports = registry.getReports(ReportType.LABEL);
        } else if (user instanceof Admin) {
            reports = registry.getReports(ReportType.ADMIN);
        } else {
            return new TypedCollection(new ArrayList<Report>(), Report.class);
        }

        return new TypedCollection(reports, Report.class);
    }

    @PostMapping(path = "/generate/{id}")
    public ResponseEntity<?> generateReport(@PathVariable String id, @RequestBody List<String> qparams) {
        Report report = registry.getReport(id);
        
        if (report == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        if(report.getId().equals("artist-royalty-report")) {
            Optional<Artist> artist = artistRepo.findById(Integer.valueOf(qparams.get(0)));
            if(!artist.isPresent())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            
            ((ArtistRoyaltyReport) report).setArtist(artist.get());
        }

        User user = authFacade.getCurrentUser();
        if (!hasAccess(report, user)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ReportGenerator generator = new ReportGenerator();
        String html = generator.generateHTML(em, report, qparams);

        return new ResponseEntity<>(html, HttpStatus.OK);
    }

    private boolean hasAccess(Report report, User user) {
        // todo
        return true;
    }
}