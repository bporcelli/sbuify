package com.cse308.sbuify.reports;

import java.util.HashMap;
import java.util.Map;

import com.cse308.sbuify.artist.Artist;

public class ArtistRoyaltyReport extends TableReport {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ARTIST_NAME_KEY = "%ARTIST_NAME%";
    private static final String ARTIST_CREATED_DATE_KEY = "%ARTIST_CREATED_DATE%";
    
    private Artist artist;

    public ArtistRoyaltyReport() {
        super("artist-royalty-report", "Artist Royalty Report", ReportType.LABEL);
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    @Override
    public String getQuery() {
        return "SELECT COUNT(*) AS count, Song.name "
                + "FROM Stream, Song WHERE Stream.song_id = Song.id and Song.album_id IN "
                + "(SELECT Album.id FROM Album, Artist "
                + "WHERE Album.artist_id = Artist.id and Artist.id = ?) "
                + "GROUP BY Song.id "
                + "ORDER BY count DESC;";
    }

    @Override
    public String getTemplatePath() {
        return "ArtistRoyaltyReport.tmpl";
    }
    
    @Override
    public Map<String, String> getReplaceMap(){
        Map<String, String> replaceMap = new HashMap<>();
        replaceMap.put(ARTIST_NAME_KEY, artist.getName());
        replaceMap.put(ARTIST_CREATED_DATE_KEY, artist.getCreatedDate().toString());
        return replaceMap;
    }
}
