package tmp;


import javax.persistence.Entity;

import com.cse308.sbuify.playlist.PlayList;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Chart extends PlayList implements Serializable {
    private Date date;
    private Chart previousChart;
}
