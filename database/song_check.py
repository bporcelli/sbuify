import os
import db

# config
_SONG_DIR = '../server/'


def do_song_check():
    db.init()

    # Delete file_paths with invalid path
    db.execute("SELECT * FROM song_files")

    for song_file in db.get_cursor().fetchall():
        if os.path.exists(os.path.join(_SONG_DIR, song_file['path'])):
            continue
        print('File {} doesn\'t exist. Deactivating song {}.'.format(
            song_file['path'], song_file['song_id']), flush=True)
        db.execute("UPDATE song SET active = FALSE WHERE id = {}".format(
            song_file['song_id']))
        db.execute("DELETE FROM song_files WHERE song_id = {} AND quality = '{}'"
            .format(song_file['song_id'], song_file['quality']))

    # wrap up
    db.close()

    print('done.')


do_song_check()
