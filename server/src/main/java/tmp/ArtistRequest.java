package tmp;

import com.cse308.sbuify.domain.Artist;
import com.cse308.sbuify.domain.RecordLabel;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class ArtistRequest implements Serializable{
    @Id
    private Integer id;
    private LocalDateTime creation;
    private RecordLabel requestTo;
    private Artist requestFrom;
}
