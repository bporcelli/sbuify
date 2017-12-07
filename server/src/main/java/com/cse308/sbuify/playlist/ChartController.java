package com.cse308.sbuify.playlist;

import com.cse308.sbuify.common.TypedCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping(path = "/api/charts")
public class ChartController {

    @Autowired
    private ChartRepository chartRepository;

    /**
     * Return all charts
     * @return
     */
    @GetMapping
    public @ResponseBody TypedCollection getAllCharts() {
        Iterable<Chart> chartsIterable = chartRepository.findAll();
        Set<Chart> charts = new HashSet<>();
        chartsIterable.forEach(chart -> charts.add(chart));
        return new TypedCollection(charts, Chart.class);
    }

    /**
     * Return specified chart
     * @param chartId
     * @return a 200 response on success, otherwise a 404 if the provided chart ID is invalid.
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getChart(@PathVariable Integer chartId){
        Chart chart = getChartById(chartId);
        if (chart == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(chart, HttpStatus.OK);
    }

    /**
     * Helper method to get chart by ID.
     * @param chartId
     * @return A Chart object, or null if the chart ID is invalid.
     */
    private Chart getChartById(Integer chartId) {
        Optional<Chart> chartOptional = chartRepository.findById(chartId);
        if (!chartOptional.isPresent()) {
            return null;
        }
        return chartOptional.get();
    }
}
