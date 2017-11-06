package com.cse308.sbuify.playlist;


import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.user.AppUser;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.Date;

@Entity
public class Chart extends Playlist {

    // The date this chart is for
    @NotNull
    private Date date;

    // The previous chart, if any
    @OneToOne
    private Chart previousChart;

    public Chart() {}

    public Chart(@NotEmpty String name, AppUser owner, Image image, @NotNull Boolean _private, @NotNull Date date) {
        super(name, owner, image, _private);
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
