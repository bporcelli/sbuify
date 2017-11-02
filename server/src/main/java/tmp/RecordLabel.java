package tmp;


import com.cse308.sbuify.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a Record Label.
 */
@Entity
@DiscriminatorValue(value = "recordlabel")
public class RecordLabel extends User implements Serializable {
    @NotNull
    private String name;

    @ElementCollection(targetClass = Artist.class)
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Artist> artists = new HashSet<>();

    public RecordLabel(@NotNull String email, @NotNull String password, @NotNull String name) {
        super(email, password);
        this.name = name;
    }

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

    @JsonIgnore
    @Override
    public String getRole() {
        return "ROLE_LABEL";
    }
}
