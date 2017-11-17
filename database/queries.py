insert_album = (
    "INSERT INTO album (name, release_date, music_brainz_id, num_songs, "
    "duration, image_id, artist_id) VALUES (%(name)s, %(release_date)s, "
    "%(music_brainz_id)s, %(num_songs)s, %(duration)s, %(image_id)s, "
    "%(artist_id)s)"
)

insert_album_song = (
    "INSERT INTO album_songs (album_id, song_id) VALUES (%s, %s)"
)

insert_song = (
    "INSERT INTO song (name, mbid, length, play_count, track_number, "
    "album_id, image_id) VALUES (%(name)s, %(mbid)s, %(length)s, "
    "%(play_count)s, %(track_number)s, %(album_id)s, %(image_id)s)"
)

insert_song_genre = (
    "INSERT INTO song_genres (song_id, genre_id) VALUES (%s, %s)"
)

insert_song_label = (
    "INSERT INTO song_label (song_id, label_id) VALUES (%s, %s)"
)

insert_image = (
    "INSERT INTO image (path, size) VALUES (%(path)s, %(size)s)"
)

insert_label = (
    "INSERT INTO label (name, music_brainz_id) VALUES (%(name)s, "
    "%(music_brainz_id)s)"
)

insert_genre = "INSERT INTO genre (name) VALUES (%(name)s)"

insert_artist = "INSERT INTO artist (name, music_brainz_id) VALUES (%s, %s)"

insert_artist_album = (
    "INSERT INTO artist_albums (artist_id, album_id) VALUES (%s, %s)"
)

insert_artist_alias = (
    "INSERT INTO artist_aliases (artist_id, alias) VALUES (%s, %s)"
)
