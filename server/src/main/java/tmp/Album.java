package tmp;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import com.cse308.sbuify.domain.CatalogItem;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
public class Album extends CatalogItem implements Queueable, Serializable {
    public Date releaseDate;
    public Double duration;
    public Integer numSongs;
    private Artist artist;
    @ElementCollection(targetClass=Song.class)
    private List<Song> songs;

    @Override
    @ElementCollection(targetClass=Song.class)
    public Collection<Song> getItems() {
        return null;
    }
}
