DROP VIEW IF EXISTS playlist_albums;

CREATE VIEW playlist_albums AS
	SELECT ps.playlist_id, s.album_id, MIN(ps.date_saved) AS date_saved
	FROM playlist_songs ps, song s
	WHERE ps.song_id = s.id
	GROUP BY playlist_id, album_id;

DROP VIEW IF EXISTS playlist_artists;

CREATE VIEW playlist_artists AS
	SELECT ps.playlist_id, a.artist_id, MIN(ps.date_saved) AS date_saved
    FROM playlist_songs ps, song s, album a
    WHERE ps.song_id = s.id
		AND s.album_id = a.id
	GROUP BY playlist_id, artist_id;