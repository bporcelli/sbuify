package tmp;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import com.cse308.sbuify.domain.Artist;
import com.cse308.sbuify.domain.CatalogItem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Concert extends  CatalogItem implements Serializable{
    @ElementCollection(targetClass= Artist.class)
    private Set<Artist> lineUp= new LinkedHashSet<>();
    private LocalDateTime time;
    private Venue venue;

}
