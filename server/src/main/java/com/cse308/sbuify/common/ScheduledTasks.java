package com.cse308.sbuify.common;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.album.AlbumRepository;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.artist.MonthlyListenersDTO;
import com.cse308.sbuify.artist.QuarterlyRoyaltyDTO;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.playlist.*;
import com.cse308.sbuify.song.Genre;
import com.cse308.sbuify.song.GenreRepository;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;
import com.cse308.sbuify.stream.Stream;
import com.cse308.sbuify.stream.StreamCountDTO;
import com.cse308.sbuify.stream.StreamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private final Integer numPlaylistsMixAndMatch;
    private final Integer playCountUpdateInterval;
    private final Integer monthlyListenersUpdateInterval;
    
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private OverviewPlaylistRepository overviewPlaylistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SongRepository songRepo;

    @Autowired
    private StreamRepository streamRepo;

    private Map<Artist, BigDecimal> royalty = new Hashtable<>();

    @Autowired
    public ScheduledTasks(ScheduledTaskProperties scheduledTaskProperties) {
        numPlaylistsMixAndMatch = scheduledTaskProperties.getNumMixAndMatch();
        playCountUpdateInterval = scheduledTaskProperties.getPlayCountUpdateInterval();
        monthlyListenersUpdateInterval = scheduledTaskProperties.getMonthlyListenersUpdateInterval();
    }

    // Run every 25th of the month
    @Scheduled(cron="0 0 9 25 * ?", zone="America/New_York")
    public void monthlyOverviewPlaylistTask() {
        try{
            createTopSongsByGenre();
            createTopSongsByArtist();
            mixAndMatchArtist(2);
            mixAndMatchArtist(3);
            mixAndMatchGenre(2);
            mixAndMatchGenre(3);
        } catch (Exception e) {
            logger.error("ERROR On Monthly Task");
        }
    }

    // Run daily at 6am
    @Scheduled(cron="0 0 6 * * *", zone="America/New_York")
    public void dailyOverviewPlaylistTask() {
        try {
            trendingCommunitySongs();
        } catch (Exception e) {
            logger.error("ERROR On Daily Task");
        }
    }

    // Run every six hours
    @Scheduled(cron="0 0 */6 * * *", zone="America/New_York")
    public void updatePlayCounts() {
        LocalDateTime timeAfter = LocalDateTime.now().minusHours(playCountUpdateInterval);
        List<StreamCountDTO> newStreams = streamRepo.getNewStreamsAfterTime(timeAfter);

        for (StreamCountDTO dto: newStreams) {
            Song song = dto.getSong();
            song.setPlayCount(song.getPlayCount() + dto.getNewStreams());
            songRepo.save(song);
        }
    }

    // Run every 30 days
    @Scheduled(cron="0 0/30 * * * ?", zone="America/New_York")
    public void updateMonthlyListeners() {
        LocalDateTime timeAfterNthHr = LocalDateTime.now().minusDays(monthlyListenersUpdateInterval);
        List<MonthlyListenersDTO> listeners = streamRepo.getNewMonthlyCountAfterTime(timeAfterNthHr);

        for(MonthlyListenersDTO dto: listeners){
            Artist artist = dto.getArtist();
            Long monthlyListener = dto.getNumListeners();
            artist.setMonthlyListeners(monthlyListener.intValue());
            artistRepository.save(artist);
        }
    }

    // Run every Quarter
    @Scheduled(cron="0 0 1 */3 * ?", zone="America/New_York")
    public void updateQuarterPayment() {
        LocalDateTime timeAfterNthHr = LocalDateTime.now().minusMonths(3);
        List<QuarterlyRoyaltyDTO> compensation = streamRepo.getQuarterlyRoyalty(timeAfterNthHr);

        for(QuarterlyRoyaltyDTO dto: compensation){
            Artist artist = dto.getArtist();
            BigDecimal payment = dto.getPayment();
            royalty.put(artist, payment);
        }
    }

    @Transactional
    protected void createTopSongsByGenre() {
        OverviewPlaylist newList = new OverviewPlaylist("Top 50 by Genre", OverviewPlaylistType.TOP_50_BY_GENRE);

        Iterable<Genre> genres = genreRepository.findAll();

        for (Genre genre: genres) {
            String genreTitle = String.format("%s Songs", genre.getName());
            Image image = genre.getImage();

            if (image != null) {
                image.setId(null);  // set image to null to prevent Hibernate merge issues
            }

            Playlist playlist = new Playlist(genreTitle, null, image,false);
            List<Song> genreSongs = songRepository.top50SongsByGenre(genre.getName());
            List<PlaylistSong> playlistSongs = new ArrayList<>();

            genreSongs.forEach(song -> playlistSongs.add(new PlaylistSong(playlist, song)));

            playlist.setSongs(playlistSongs);
            newList.addPlaylist(playlist);
        }

        // delete the old playlist of this type
        overviewPlaylistRepository.deleteByType(OverviewPlaylistType.TOP_50_BY_GENRE);
        
        // save new playlist
        overviewPlaylistRepository.save(newList);
    }

    @Transactional
    protected void createTopSongsByArtist() {
        OverviewPlaylist overviewPlaylist = new OverviewPlaylist("Top 50 By Artist",
                OverviewPlaylistType.TOP_50_BY_ARTIST);

        Iterable<Artist> artists = artistRepository.findAll();

        for (Artist artist: artists) {
            String playlistTitle = String.format("%s Top 50", artist.getName());
            Image image = (Image) artist.getImage();

            if (image != null) {
                image.setId(null);
            }

            Playlist playlist = new Playlist(playlistTitle, null, image,false);
            List<Song> artistSong = songRepository.top50SongsByArtist(artist.getId());
            List<PlaylistSong> playlistSongs = new ArrayList<>();

            artistSong.forEach(song -> playlistSongs.add(new PlaylistSong(playlist, song)));

            playlist.setSongs(playlistSongs);
            overviewPlaylist.addPlaylist(playlist);
        }

        // delete old playlist of type
        overviewPlaylistRepository.deleteByType(OverviewPlaylistType.TOP_50_BY_ARTIST);

        // save new playlist
        overviewPlaylistRepository.save(overviewPlaylist);
    }

    @Transactional
    protected void mixAndMatchArtist(Integer artistsPerPlaylist) {
        OverviewPlaylist overviewPlaylist;

        if (artistsPerPlaylist.equals(2)) {
            overviewPlaylist = new OverviewPlaylist("Artist Duo Mix and Match",
                    OverviewPlaylistType.MONTHLY_ARTIST_DUO);
        } else {
            overviewPlaylist = new OverviewPlaylist("Artist Triplet Mix and Match",
                    OverviewPlaylistType.MONTHLY_ARTIST_TRIPLET);
        }

        Long totalArtists = artistRepository.count();

        // generate unique 2-tuples or 3-tuples of artists
        Set<Long[]> tuples = new HashSet<>();

        Random random = new Random();

        while (tuples.size() < numPlaylistsMixAndMatch) {
            Long[] artistIndices = new Long[artistsPerPlaylist];

            int length = 0;
            while (length < artistsPerPlaylist) {
                long index = Math.round(random.nextFloat() * totalArtists);
                artistIndices[length++] = index;
            }

            tuples.add(artistIndices);
        }

        // generate a playlist for each artist tuple
        for (Long[] artistTuple: tuples) {
            Artist[] artists = new Artist[artistsPerPlaylist];

            for (int i = 0; i < artistsPerPlaylist; i++) {
                Artist artist = artistRepository.getArtistByOffset(artistTuple[i]);
                artists[i] = artist;
            }

            String title;

            if (artistsPerPlaylist.equals(2)) {
                title = String.format("%s & %s", artists[0].getName(), artists[1].getName());
            } else {
                title = String.format("%s, %s & %s",
                        artists[0].getName(),
                        artists[1].getName(),
                        artists[2].getName());
            }

            // choose one of the artist's images to use as the playlist image
            int index = random.nextInt(artistsPerPlaylist);

            Image image = (Image) artists[index].getImage();

            if (image != null) {
                image.setId(null);
            }

            Playlist playlist = new Playlist(title, null, image, false);
            List<Song> artistSongs;

            if (artistsPerPlaylist == 2) {
                artistSongs = songRepository.getDistinctByArtistDuo(artists[0].getId(), artists[1].getId());
            } else {
                artistSongs = songRepository.getDistinctByArtistTriplet(artists[0].getId(),
                        artists[1].getId(), artists[2].getId());
            }

            List<PlaylistSong> playlistSongs = new ArrayList<>();

            artistSongs.forEach(song -> playlistSongs.add(new PlaylistSong(playlist, song)));

            playlist.setSongs(playlistSongs);
            overviewPlaylist.addPlaylist(playlist);
        }

        // delete old playlist
        if (artistsPerPlaylist == 2) {
            overviewPlaylistRepository.deleteByType(OverviewPlaylistType.MONTHLY_ARTIST_DUO);
        } else {
            overviewPlaylistRepository.deleteByType(OverviewPlaylistType.MONTHLY_ARTIST_TRIPLET);
        }

        // save new
        overviewPlaylistRepository.save(overviewPlaylist);
    }

    @Transactional
    public void mixAndMatchGenre(Integer genresPerPlaylist) {
        OverviewPlaylist overviewPlaylist;

        if (genresPerPlaylist == 2) {
            overviewPlaylist = new OverviewPlaylist("Genre Duo Mix and Match",
                    OverviewPlaylistType.MONTHLY_GENRE_DUO);
        } else {
            overviewPlaylist = new OverviewPlaylist("Genre Triplet Mix and Match",
                    OverviewPlaylistType.MONTHLY_GENRE_TRIPLET);
        }

        Long totalGenres = genreRepository.count();

        // generate unique 2-tuples or 3-tuples of genres
        Set<Long[]> tuples = new HashSet<>();

        Random random = new Random();

        while (tuples.size() < numPlaylistsMixAndMatch) {
            Long[] genreOffsets = new Long[genresPerPlaylist];

            int length = 0;
            while (length < genresPerPlaylist) {
                long index = Math.round(random.nextFloat() * totalGenres);
                genreOffsets[length++] = index;
                length++;
            }

            tuples.add(genreOffsets);
        }

        for (Long[] genreTuple: tuples) {
            Genre[] genres = new Genre[genresPerPlaylist];

            for (int i = 0; i < genresPerPlaylist; i++) {
                Genre genre = genreRepository.getByOffset(genreTuple[i]);
                genres[i] = genre;
            }

            String title;

            if (genresPerPlaylist == 2) {
                title = String.format("%s & %s", genres[0].getName(), genres[1].getName());
            } else {
                title = String.format("%s, %s & %s",
                        genres[0].getName(),
                        genres[1].getName(),
                        genres[2].getName());
            }

            // choose a genre image to use for the playlist at random
            int index = random.nextInt(genresPerPlaylist);

            Image image = genres[index].getImage();

            if (image != null) {
                image.setId(null);
            }

            Playlist playlist = new Playlist(title, null, image,false);
            List<Song> artistSongs;

            if (genresPerPlaylist == 2) {
                artistSongs = songRepository.getDistinctByGenreDuo(genres[0].getId(), genres[1].getId());
            } else {
                artistSongs = songRepository.getDistinctByGenreTripler(genres[0].getId(), genres[1].getId(),
                        genres[2].getId());
            }

            List<PlaylistSong> playlistSongs = new ArrayList<>();

            artistSongs.forEach(song -> playlistSongs.add(new PlaylistSong(playlist, song)));

            playlist.setSongs(playlistSongs);
            overviewPlaylist.addPlaylist(playlist);
        }

        // remove old playlist
        if (genresPerPlaylist == 2) {
            overviewPlaylistRepository.deleteByType(OverviewPlaylistType.MONTHLY_GENRE_DUO);
        } else {
            overviewPlaylistRepository.deleteByType(OverviewPlaylistType.MONTHLY_ARTIST_TRIPLET);
        }

        // save new
        overviewPlaylistRepository.save(overviewPlaylist);
    }

    @Transactional
    protected void trendingCommunitySongs() {
        OverviewPlaylist overviewPlaylist = new OverviewPlaylist("Trending Today",
                OverviewPlaylistType.DAILY_TRENDS);

        // top 50 songs by play count
        List<Song> top50PopularSongs = songRepository.getTop50PopularSongs();
        Image image = getFirstImage(top50PopularSongs);
        if (image != null) {
            image.setId(null);
        }
        Playlist popularPlaylist = new Playlist("Top 50 Songs", null, image,false);
        List<PlaylistSong> top50Songs = new ArrayList<>();

        top50PopularSongs.forEach(song -> top50Songs.add(new PlaylistSong(popularPlaylist, song)));

        popularPlaylist.setSongs(top50Songs);
        overviewPlaylist.addPlaylist(popularPlaylist);

        // bottom 50 songs by play count
        List<Song> bottom50Songs = songRepository.getTop50UnpopularSongs();
        image = getFirstImage(bottom50Songs);
        if (image != null) {
            image.setId(null);
        }
        Playlist unpopularPlaylist = new Playlist("Bottom 50 Songs", null, image,false);
        List<PlaylistSong> bottom50PlaylistSongs = new ArrayList<>();

        bottom50Songs.forEach(song -> bottom50PlaylistSongs.add(new PlaylistSong(unpopularPlaylist, song)));

        unpopularPlaylist.setSongs(bottom50PlaylistSongs);
        overviewPlaylist.addPlaylist(unpopularPlaylist);

        // out with the old...
        overviewPlaylistRepository.deleteByType(OverviewPlaylistType.DAILY_TRENDS);

        // ... and in with the new
        overviewPlaylistRepository.save(overviewPlaylist);
    }

    private Image getFirstImage(List<Song> songs){
        for (Song song: songs) {
            Album album = getAlbumById(song.getAlbum().getId());
            if (album == null) {
                continue;
            }
            if (album.getImage() == null) {
                continue;
            }
            return (Image) album.getImage();
        }
        return null;
    }

    private Album getAlbumById(Integer albumId) {
        Optional<Album> albumOptional = albumRepository.findById(albumId);
        if (!albumOptional.isPresent()) {
            return null;
        }
        return albumOptional.get();
    }

    private Artist getArtistById(Integer artistId) {
        Optional<Artist> artist = artistRepository.findById(artistId);
        if (!artist.isPresent()) {
            return null;
        }
        return artist.get();
    }
}
