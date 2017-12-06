package com.cse308.sbuify.artist;

import com.cse308.sbuify.common.api.DecoratorRegistry;
import com.cse308.sbuify.common.api.ResponseDecorator;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;

import java.util.List;

public class ArtistDecorator implements ResponseDecorator<Artist> {

    private SongRepository songRepository;

    public ArtistDecorator(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(Artist.class);
    }

    @Override
    public void decorate(Artist artist) {
        List<Song> popularSongs = songRepository.getPopularByArtist(artist.getId());

        // fixme: come up with a better way to avoid infinite recursion during serialization
        Artist artistClone;
        try {
            artistClone = (Artist) artist.clone();
        } catch (CloneNotSupportedException e) {
            return;
        }

        ResponseDecorator<Song> decorator = DecoratorRegistry.getDecorator(Song.class);

        for (Song song: popularSongs) {
            decorator.decorate(song);
            song.getAlbum().setArtist(artistClone);
        }
        artist.set("popularSongs", popularSongs);
    }
}
