set foreign_key_checks = 0;

truncate artist;
truncate artist_albums;
truncate artist_aliases;
truncate album;
truncate album_songs;
truncate image;
truncate label;
truncate song;
truncate song_featured_artists;
truncate song_genres;
truncate song_label;
truncate genre;

set foreign_key_checks = 1;