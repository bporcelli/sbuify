-- old_length is old song length (ms), new_length is new song length (ms)
create procedure update_album_length(in album_id int, in old_length real, in new_length real)
	update album set duration = duration - old_length + new_length WHERE id = album_id;

create trigger update_album_duration_on_insert
	after insert on song
    for each row call update_album_length(NEW.album_id, 0, NEW.length);

create trigger update_album_duration_on_update
	after update on song
    for each row call update_album_length(NEW.album_id, OLD.length, NEW.length);

create trigger update_album_duration_on_delete
	before delete on song
    for each row call update_album_length(OLD.album_id, OLD.length, 0);