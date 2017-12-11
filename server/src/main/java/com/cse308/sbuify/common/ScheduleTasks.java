package com.cse308.sbuify.common;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.album.AlbumRepository;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.playlist.OverviewPlaylist;
import com.cse308.sbuify.playlist.OverviewPlaylistRepository;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.playlist.PlaylistSong;
import com.cse308.sbuify.song.Genre;
import com.cse308.sbuify.song.GenreRepository;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;
import com.cse308.sbuify.stream.Stream;
import com.cse308.sbuify.stream.StreamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class ScheduleTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleTasks.class);

    private final Integer top_50_by_genre;
    private final Integer top_50_by_artist;
    private final Integer monthly_artist_duo;
    private final Integer monthly_artist_triplet;
    private final Integer monthly_genre_duo;
    private final Integer monthly_genre_triplet;
    private final Integer sbu_community;
    private final Integer playlist_mix_n_match;
    private final Integer every_nth_hr;
    private final Integer every_nth_day;
    
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

    @Autowired
    public ScheduleTasks(ScheduleTasksProperties scheduleTasksProperties){

        top_50_by_genre = scheduleTasksProperties.getTop_50_by_genre();
        top_50_by_artist = scheduleTasksProperties.getTop_50_by_artist();
        monthly_artist_duo = scheduleTasksProperties.getMonthly_artist_duo();
        monthly_artist_triplet = scheduleTasksProperties.getMonthly_artist_triplet();
        monthly_genre_duo = scheduleTasksProperties.getMonthly_genre_duo();
        monthly_genre_triplet = scheduleTasksProperties.getMonthly_genre_triplet();
        sbu_community = scheduleTasksProperties.getSbu_community();
        playlist_mix_n_match = scheduleTasksProperties.getPlaylist_mix_n_match();
        every_nth_hr = scheduleTasksProperties.getEvery_nth_hour();
        every_nth_day = scheduleTasksProperties.getEvery_nth_day();
    }

    // Run every 25th of the month
    @Scheduled(cron="0 0 9 25 * ?",zone="America/New_York")
    public void monthlyOverviewPlaylistTask(){
        try{
            createTopSongsByGenre();
            createTopSongsByArtist();
            mixAndMatchArtist(2);
            mixAndMatchArtist(3);
            mixAndMatchGenre(2);
            mixAndMatchGenre(3);
        } catch (Exception e){
            logger.error("ERROR On Monthly Task");
        }

    }

    // Run daily at 6am
    @Scheduled(cron="0 0 6 * * *", zone="America/New_York")
    public void dailyOverviewPlaylistTask(){
        try{
            trendingCommunitySongs();
            unPopularCommunitySongs();
        } catch (Exception e){
            logger.error("ERROR On Daily Task");
        }
    }

    //Run every six hours
    @Scheduled(cron="0 0 */6 * * *", zone="America/New_York")
    public void updatePlayCount(){
        LocalDateTime timeAfterNthHr = LocalDateTime.now().minusHours(every_nth_hr);
        List<Stream> streamsAfter = streamRepo.getAllByTimeAfter(timeAfterNthHr);
        Map<Integer,Integer> map = new Hashtable<>();

        for(Stream stream: streamsAfter){
            Integer songId = stream.getSong().getId();
            Integer streamCount = map.get(songId);
            if (streamCount == null){
                streamCount = 1;
            } else {
                streamCount++;
            }
            map.put(songId, streamCount);
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            Song song = getSongById(key);
            if (song == null){
                continue;
            }
            song.setPlayCount(song.getPlayCount() + value);
            songRepo.save(song);
        }
    }

    //Run every 30 days
    @Scheduled(cron="0 0/30 * * * ?", zone="America/New_York")
    public void updateMonthlyListeners(){
        LocalDateTime timeAfterNthHr = LocalDateTime.now().minusDays(every_nth_day);
        List<Stream> streamsAfter = streamRepo.getAllByTimeAfter(timeAfterNthHr);
        Map<Integer,Integer> map = new Hashtable<>();
        for(Stream stream: streamsAfter){
            Integer artistId = stream.getSong().getAlbum().getArtist().getId();
            Integer streamCount = map.get(artistId);
            if (streamCount == null){
                streamCount = 1;
            } else {
                streamCount++;
            }
            map.put(artistId, streamCount);
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            Artist artist = getArtistById(key);
            if (artist == null){
                continue;
            }
            artist.setMonthlyListeners(value);
            artistRepository.save(artist);
        }
    }

    private Song getSongById(Integer songId){
        Optional<Song> songOptional = songRepo.findById(songId);
        if(!songOptional.isPresent()){
            return null;
        }
        return songOptional.get();
    }

    private void createTopSongsByGenre() throws Exception {
        OverviewPlaylist overviewPlaylist = getOverviewPlaylistById(top_50_by_genre);

        if (overviewPlaylist == null){
            throw new Exception();
        }

        overviewPlaylist.clearPlaylist();

        Iterable<Genre> genreList = genreRepository.findAll();
        List<Genre> genres = new ArrayList<>();
        genreList.forEach(genre -> genres.add(genre));

        for (Genre genre: genres){
            String genreTitle = String.format("%s Songs",genre.getName());
            Image image = genre.getImage();

            if (image != null){
                image.setId(null);
            }

            Playlist playlist = new Playlist(genreTitle, null, image,false);
            List<Song> genreSongs = songRepository.top50SongsByGenre(genre.getName());
            List<PlaylistSong> playlistSongs = new ArrayList<>();

            genreSongs.forEach(song -> {
                playlistSongs.add(new PlaylistSong(playlist, song));
            });

            playlist.setSongs(playlistSongs);
            overviewPlaylist.addPlaylist(playlist);
        }
        overviewPlaylistRepository.save(overviewPlaylist);
    }

    public void createTopSongsByArtist() throws Exception {

        OverviewPlaylist overviewPlaylist = getOverviewPlaylistById(top_50_by_artist);

        if (overviewPlaylist == null){
            throw new Exception();
        }

        overviewPlaylist.clearPlaylist();

        Iterable<Artist> artistsList = artistRepository.findAll();
        List<Artist> artists = new ArrayList<>();
        artistsList.forEach(artist -> artists.add(artist));

        for (Artist artist: artists){
            String genreTitle = String.format("%s Songs",artist.getName());
            Image image = (Image)artist.getImage();

            if (image != null){
                image.setId(null);
            }

            Playlist playlist = new Playlist(genreTitle, null, image,false);
            List<Song> artistSong = songRepository.top50SongsByArtist(artist.getId());
            List<PlaylistSong> playlistSongs = new ArrayList<>();

            artistSong.forEach(song -> {
                playlistSongs.add(new PlaylistSong(playlist, song));
            });

            playlist.setSongs(playlistSongs);
            overviewPlaylist.addPlaylist(playlist);
        }
        overviewPlaylistRepository.save(overviewPlaylist);
    }

    private void mixAndMatchArtist(Integer number) throws Exception {

        OverviewPlaylist overviewPlaylist = getOverviewPlaylistById(number.equals(2)?
                                                                    monthly_artist_duo :
                                                                    monthly_artist_triplet);

        if (overviewPlaylist == null){
            throw new Exception();
        }

        overviewPlaylist.clearPlaylist();

        ArrayList<Integer[]> namesArray = new ArrayList<>();
        Iterable<Artist> getAllArtist = artistRepository.findAll();
        ArrayList<Artist> allArtist = new ArrayList<>();

        getAllArtist.forEach(artist -> {
            allArtist.add(artist);
        });

        int count = 0;
        while (count < playlist_mix_n_match){
            Integer[] mixNumber = new Integer[number];
            int length = 0;

            while(length < number){
                Random random = new Random();
                int index = random.nextInt(allArtist.size());
                Artist selected = allArtist.get(index);
                mixNumber[length] = selected.getId();
                length++;
            }

            boolean exist = namesArray.parallelStream().anyMatch( list -> {
                return list.hashCode() == mixNumber.hashCode();
            });

            if(!exist){
                namesArray.add(mixNumber);
                count++;
            }
        }

        for (Integer[] artistId : namesArray){

            ArrayList<Artist> artists = new ArrayList<>();
            for(int i = 0; i < number; i++){

                Artist artist = getArtistById(artistId[i]);

                if (artist == null){
                    throw new Exception("Unable to locate");
                }
                artists.add(artist);
            }

            String title;

            if (number.equals(2)){
                title = String.format("%s & %s's playlist", artists.get(0).getName(),artists.get(1).getName());
            } else {
                title = String.format("%s & %s & %s's playlist",
                        artists.get(0).getName(),
                        artists.get(1).getName(),
                        artists.get(2).getName());
            }

            Random random = new Random();
            int index = random.nextInt(number);
            Image image;
            image = (Image)artists.get(index).getImage();

            if (image != null){
                image.setId(null);
            }

            Playlist playlist = new Playlist(title, null, image, false);
            List<Song> artistSongs;

            if (number == 2){
                artistSongs = songRepository.artistDuo(artistId[0], artistId[1]);
            } else {
                artistSongs = songRepository.artistTriplet(artistId[0], artistId[1], artistId[2]);
            }

            List<PlaylistSong> playlistSongs = new ArrayList<>();

            artistSongs.forEach(song -> {
                playlistSongs.add(new PlaylistSong(playlist, song));
            });

            playlist.setSongs(playlistSongs);
            overviewPlaylist.addPlaylist(playlist);
        }
        overviewPlaylistRepository.save(overviewPlaylist);
    }

    public void mixAndMatchGenre(Integer number) throws Exception {
        OverviewPlaylist overviewPlaylist = getOverviewPlaylistById(number.equals(2)?
                                                                    monthly_genre_duo:
                                                                    monthly_genre_triplet);

        if (overviewPlaylist == null){
            throw new Exception();
        }

        overviewPlaylist.clearPlaylist();

        ArrayList<Integer[]> namesArray = new ArrayList<>();
        Iterable<Genre> getAllGenre = genreRepository.findAll();
        ArrayList<Genre> allGenre = new ArrayList<>();

        getAllGenre.forEach(artist -> {
            allGenre.add(artist);
        });

        int count = 0;
        while (count < playlist_mix_n_match){

            Integer[] mixNumber = new Integer[number];
            int length = 0;

            while(length < number){
                Random random = new Random();
                int index = random.nextInt(allGenre.size());
                Genre selected = allGenre.get(index);
                mixNumber[length] = selected.getId();
                length++;
            }

            boolean exist = namesArray.parallelStream().anyMatch( list -> {
                return list.hashCode() == mixNumber.hashCode();
            });

            if(!exist){
                namesArray.add(mixNumber);
                count++;
            }
        }

        for (Integer[] genreId : namesArray){
            ArrayList<Genre> genres = new ArrayList<>();

            for(int i = 0; i < number; i++){
                Genre genre = getGenreById(genreId[i]);
                if (genre == null){
                    throw new Exception("Unable to locate");
                }
                genres.add(genre);
            }

            String title;

            if (genreId.length == 2){
                title = String.format("%s & %s's songs", genres.get(0).getName(), genres.get(1).getName());
            } else {
                title = String.format("%s & %s & %s's songs",
                        genres.get(0).getName(),
                        genres.get(1).getName(),
                        genres.get(2).getName());
            }

            Random random = new Random();
            int index = random.nextInt(number);

            Image image;

            image = genres.get(index).getImage();

            if (image != null){
                image.setId(null);
            }

            Playlist playlist = new Playlist(title, null, image,false);
            List<Song> artistSongs;

            if (number == 2){
                artistSongs = songRepository.genreDuo( genreId[0], genreId[1]);
            } else {
                artistSongs = songRepository.genreTriplet( genreId[0], genreId[1], genreId[2]);
            }
            List<PlaylistSong> playlistSongs = new ArrayList<>();

            artistSongs.forEach(song -> {
                playlistSongs.add(new PlaylistSong(playlist, song));
            });

            playlist.setSongs(playlistSongs);
            overviewPlaylist.addPlaylist(playlist);
        }
        overviewPlaylistRepository.save(overviewPlaylist);
    }

    private void trendingCommunitySongs() throws Exception {
        OverviewPlaylist overviewPlaylist = getOverviewPlaylistById(sbu_community);

        if (overviewPlaylist == null){
            throw new Exception();
        }

        overviewPlaylist.clearPlaylist();

        List<Song> top50PopularSongs = songRepository.getTop50PopularSongs();

        Image image = getFirstImage(top50PopularSongs);
        if (image != null){
            image.setId(null);
        }

        Playlist playlist = new Playlist("Top 50 Popular Songs", null, image,false);
        List<PlaylistSong> playlistSongs = new ArrayList<>();

        top50PopularSongs.forEach(song -> { playlistSongs.add(new PlaylistSong(playlist, song));
        });

        playlist.setSongs(playlistSongs);
        overviewPlaylist.addPlaylist(playlist);
        overviewPlaylistRepository.save(overviewPlaylist);
    }

    public void unPopularCommunitySongs() throws Exception {
        OverviewPlaylist overviewPlaylist = getOverviewPlaylistById(sbu_community);

        if (overviewPlaylist == null){
            throw new Exception();
        }

        List<Song> top50unPopularSongs = songRepository.getTop50UnpopularSongs();
        Image image = getFirstImage(top50unPopularSongs);
        if (image != null){
            image.setId(null);
        }
        Playlist playlist = new Playlist("Top 50 Unpopular Songs", null, image,false);
        List<PlaylistSong> playlistSongs = new ArrayList<>();

        top50unPopularSongs.forEach(song -> { playlistSongs.add(new PlaylistSong(playlist, song));
        });

        playlist.setSongs(playlistSongs);
        overviewPlaylist.addPlaylist(playlist);
        overviewPlaylistRepository.save(overviewPlaylist);
    }

    private Image getFirstImage(List<Song> songs){
        for (Song song: songs){
            Album album = getAlbumById(song.getAlbum().getId());
            if (album == null){
                continue;
            }
            if (album.getImage() == null){
                continue;
            }
            return (Image)album.getImage();
        }
        return null;

    }

    private Album getAlbumById(Integer albumId){
        Optional<Album> albumOptional = albumRepository.findById(albumId);
        if(!albumOptional.isPresent()){
            return null;
        }
        return albumOptional.get();
    }

    private Artist getArtistById(Integer artistId){
        Optional<Artist> artist = artistRepository.findById(artistId);
        if(!artist.isPresent()){
            return null;
        }
        return artist.get();
    }

    private Genre getGenreById(Integer genreId){
        Optional<Genre> genre = genreRepository.findById(genreId);
        if(!genre.isPresent()){
            return null;
        }
        return genre.get();
    }

    private OverviewPlaylist getOverviewPlaylistById(Integer Id){
        Optional<OverviewPlaylist> optionalOverviewPlaylist = overviewPlaylistRepository.findById(Id);
        if (!optionalOverviewPlaylist.isPresent()){
            return null;
        }
        return optionalOverviewPlaylist.get();
    }
}
