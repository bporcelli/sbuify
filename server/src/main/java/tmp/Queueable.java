package tmp;

import com.cse308.sbuify.music.Song;

import java.util.Collection;

public interface Queueable {
    Collection<Song> getItems();
}
