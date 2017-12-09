-- update album length when a song's length changes
CREATE PROCEDURE update_album_length
                (IN album_id int,
                 IN old_song_length int,
                 IN new_song_length int)
UPDATE album
SET length = length - old_song_length + new_song_length
WHERE id = album_id;

-- update the number of song's for an album when a song is inserted or deleted
CREATE PROCEDURE update_album_song_count
                (IN album_id int,
                 IN delta int)
UPDATE album
SET num_songs = num_songs + delta
WHERE id = album_id;


-- update playlist length when a song is added or removed
CREATE PROCEDURE update_playlist_length
                (IN playlist_id int)
UPDATE playlist p
LEFT JOIN (
	SELECT p.playlist_id AS playlist_id, SUM(s.length) AS length
    FROM playlist_songs p, song s
    WHERE p.song_id = s.id
    GROUP BY p.playlist_id
) pl
ON pl.playlist_id = p.id
SET p.length = IF(ISNULL(pl.length), 0, pl.length)
WHERE p.id = playlist_id;

-- update the number of songs in a playlist when a song is added or removed
CREATE PROCEDURE update_playlist_song_count
                (IN playlist_id int,
                 IN delta int)
UPDATE playlist
SET num_songs = num_songs + delta
WHERE id = playlist_id;

-- update the lengths of all playlists containing a song when the song is updated
CREATE PROCEDURE update_playlists_with_song
				(IN song_id int)
UPDATE playlist p
LEFT JOIN (
	SELECT p.playlist_id AS playlist_id, SUM(s.length) AS length
    FROM playlist_songs p, song s
    WHERE p.song_id = s.id
    GROUP BY p.playlist_id
) pl
ON pl.playlist_id = p.id
SET p.length = IF(ISNULL(pl.length), 0, pl.length)
WHERE p.id > 0
AND EXISTS (
	SELECT *
    FROM playlist_songs ps
    WHERE ps.playlist_id = p.id
    AND ps.song_id = song_id
);


DELIMITER $$

CREATE TRIGGER update_album_when_song_inserted
AFTER INSERT ON song
FOR EACH ROW
BEGIN
    CALL update_album_length(NEW.album_id, 0, NEW.LENGTH);
    CALL update_album_song_count(NEW.album_id, 1);
END
$$

CREATE TRIGGER update_album_when_song_deleted
BEFORE DELETE ON song
FOR EACH ROW
BEGIN
    CALL update_album_length(OLD.album_id, OLD.LENGTH, 0);
    CALL update_album_song_count(OLD.album_id, -1);
END
$$

CREATE TRIGGER update_album_and_playlist_when_song_updated
AFTER UPDATE ON song
FOR EACH ROW
BEGIN
    CALL update_album_length(NEW.album_id, OLD.LENGTH, NEW.LENGTH);
    CALL update_playlists_with_song(NEW.id);
END
$$


CREATE TRIGGER update_playlist_when_song_inserted
AFTER INSERT ON playlist_songs
FOR EACH ROW
BEGIN
    CALL update_playlist_length(NEW.playlist_id);
    CALL update_playlist_song_count(NEW.playlist_id, 1);
END
$$

CREATE TRIGGER update_playlist_when_song_deleted
AFTER DELETE ON playlist_songs
FOR EACH ROW
BEGIN
    CALL update_playlist_length(OLD.playlist_id);
    CALL update_playlist_song_count(OLD.playlist_id, -1);
END
$$

DELIMITER ;