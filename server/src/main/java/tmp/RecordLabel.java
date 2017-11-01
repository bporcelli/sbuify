package tmp;


import org.hibernate.annotations.ManyToAny;

import com.cse308.sbuify.domain.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/*
    Record Label entity: Subclass of User
    Responsible for artist operations

 */

@Entity
@DiscriminatorValue(value = "recordlabel")
public class RecordLabel extends User implements Serializable{
    @NotNull
    private String name;

    @ElementCollection(targetClass= Artist.class)
    @OneToMany(
            cascade = CascadeType.ALL
    )
    private Set<Artist> artists = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }
}
