package tmp;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cse308.sbuify.domain.Image;

import java.io.Serializable;
import java.util.List;

@Entity

public class Biography implements Serializable {
    @Id
    private Integer id;
    private String bio;
    @ElementCollection(targetClass=Image.class)
    private List<Image> images;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
