package tmp;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.cse308.sbuify.domain.Customer;
import com.cse308.sbuify.domain.PlayList;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Stream implements Serializable {
    @Id
    private Integer id;
    private  Boolean isPremium;
    private LocalDateTime time;
    private PlayList playList;
    @ElementCollection(targetClass=TimeRange.class)
    private List<TimeRange> timeRanges;
    private Customer customer;
    private Song song;
}
