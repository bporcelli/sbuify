set foreign_key_checks = 0;

delete from `image` where `id` in (
    select distinct (`image_id`) from artist
    union
    select distinct (`cover_image_id`) from artist
    union
    select distinct (`image_id`) from album
);

truncate artist;
truncate artist_albums;
truncate artist_aliases;
truncate album;
truncate album_songs;
truncate label_song;
truncate song;
truncate song_featured_artists;
truncate song_genres;
truncate song_files;

set foreign_key_checks = 1;