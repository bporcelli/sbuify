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

DELIMITER ;

CREATE TRIGGER update_album_when_song_updated
AFTER UPDATE ON song
FOR EACH ROW
    CALL update_album_length(NEW.album_id, OLD.LENGTH, NEW.LENGTH);