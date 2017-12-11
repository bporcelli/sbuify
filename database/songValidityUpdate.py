import os
import db

# config
_SONG_DIR = '../server/static/songs/'

filenames = [f.name for f in os.scandir(_SONG_DIR) if f.is_file()]

def detect_valid_songs(filename):
    return filename in filenames;

def do_song_check():
    db.init()
    # Delete file_paths with invalid path
    db.execute("SELECT * FROM song_files")
    songs = db.get_cursor().fetchall()
    for song_files in songs:
        print('checking path {}...'.format(song_files['path']))
        if (not (detect_valid_songs(song_files['path'][13:]))):
            print('Not found {}'.format(song_files['path'][13:]))
            print('Deactivating song id {}'.format(song_files['song_id']))
            db.execute("UPDATE song SET active = FALSE WHERE song.id = " + str(song_files['song_id']))
            print('Deleting file path {}'.format(song_files['path']))
            db.execute("DELETE FROM song_files WHERE song_id = " + str(song_files['song_id']))
        print('Path found')

    print('done.', flush=True)
    # wrap up
    print('finishing...')
    print('done.')
    db.close()
    
do_song_check();




