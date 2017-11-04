package com.cse308.sbuify.playlist;


import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import java.util.Date;

@Entity
public class Chart extends Playlist {

    @NotNull
    private Date date;

    private Chart previousChart;

    public Chart() {
    }

    public Chart(@NotNull Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Chart getPreviousChart() {
        return previousChart;
    }

    public void setPreviousChart(Chart previousChart) {
        this.previousChart = previousChart;
    }
}
