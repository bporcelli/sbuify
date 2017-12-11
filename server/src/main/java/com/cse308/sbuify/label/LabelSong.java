package com.cse308.sbuify.label;

import com.cse308.sbuify.song.Song;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Associates record labels with songs they hold the rights for.
 */
@Entity
@IdClass(LabelSong.PK.class)
public class LabelSong {

    @Id
    @ManyToOne
    private Label label;

    @Id
    @ManyToOne
    private Song song;

    public LabelSong() {
    }

    public LabelSong(Label label, Song song) {
        this.label = label;
        this.song = song;
    }

    public static class PK implements Serializable {
        private Integer label;
        private Integer song;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PK pk = (PK) o;

            if (label != null ? !label.equals(pk.label) : pk.label != null) return false;
            return song != null ? song.equals(pk.song) : pk.song == null;
        }

        @Override
        public int hashCode() {
            int result = label != null ? label.hashCode() : 0;
            result = 31 * result + (song != null ? song.hashCode() : 0);
            return result;
        }
    }
}
