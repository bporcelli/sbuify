"""Database queries used for data import."""

insert_album = '''
INSERT INTO album
            (name,
             mbid,
             type,
             release_date,
             image_id,
             artist_id)
VALUES      (%(name)s,
             %(mbid)s,
             %(type)s,
             %(release_date)s,
             %(image_id)s,
             %(artist_id)s);
'''

insert_album_song = '''
INSERT INTO album_songs
            (album_id,
             song_id)
VALUES      (%s,
             %s);
'''

insert_song = '''
INSERT INTO song
            (name,
             mbid,
             length,
             track_number,
             album_id,
             image_id,
             lyrics)
VALUES      (%(name)s,
             %(mbid)s,
             %(length)s,
             %(track_number)s,
             %(album_id)s,
             %(image_id)s,
             %(lyrics)s);
'''

insert_song_genre = '''
INSERT INTO song_genres
            (song_id,
             genre_id)
VALUES      (%s,
             %s);
'''

insert_label_song = '''
INSERT INTO label_song
            (label_id,
             song_id)
VALUES      (%s,
             %s);
'''

insert_image = '''
INSERT INTO image
            (width,
             height)
VALUES      (%(width)s,
             %(height)s);
'''

insert_image_size = '''
INSERT INTO image_sizes
            (`image_id`,
             `size`,
             `path`)
VALUES      (%(image_id)s,
             %(size)s,
             %(path)s);
'''

insert_label = '''
INSERT INTO label
            (name,
             mbid)
VALUES      (%(name)s,
             %(mbid)s);
'''

insert_genre = '''
INSERT INTO genre
            (name)
VALUES      (%(name)s);
'''

insert_artist = '''
INSERT INTO artist
            (name,
             mbid,
             image_id,
             cover_image_id)
VALUES      (%(name)s,
             %(mbid)s,
             %(image_id)s,
             %(cover_image_id)s);
'''

insert_artist_album = '''
INSERT INTO artist_albums
            (artist_id,
             album_id)
VALUES      (%s,
             %s);
'''

insert_artist_alias = '''
INSERT INTO artist_aliases
            (artist_id,
             alias)
VALUES      (%s,
             %s);
'''

insert_song_featured_artists = '''
INSERT INTO song_featured_artists
            (song_id,
             artist_id)
VALUES      (%s, %s);
'''
