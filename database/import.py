from math import ceil
from mysql import connector
from datetime import datetime
from musicbrainzngs.musicbrainz import ResponseError
from musicbrainzngs.musicbrainz import NetworkError
import musicbrainzngs
import random
import queries
import time
import shutil
import os

# todo: fix duplicated albums/cover art (release groups instead of releases)
# todo: clean keys in saved method

# CONFIG
DB_USER = 'root'
DB_PASS = '1234'
DB_HOST = 'localhost'
DB_NAME = 'test'
RELEASES_PER_PAGE = 100
RELEASES_PER_LABEL = 500
RELEASE_EXTRAS = ['artists', 'media', 'recordings',
                  'artist-credits', 'tags', 'aliases']
RETRY_SECS = 5
LABELS = {
    'RCA': '1ca5ed29-e00b-4ea5-b817-0bcca0e04946',
    'Columbia': '011d1192-6f65-45bd-85c4-0400dd45693e',
    'Epic': '8f638ddb-131a-4cc3-b3d4-7ebdac201b55',
    'Warner Bros.': 'c595c289-47ce-4fba-b999-b87503e8cb71',
    'Atlantic': '50c384a2-0b44-401b-b893-8181173339c7'
}
TRUNCATE_SQL = './sql/truncate.sql'
IMAGE_DIR = '../content/images/'


# GLOBALS
conn = None
cursor = None
saved_entities = {}  # map from mb keys to internal ids


# HELPERS

def get_label_releases(mbid, offset, limit):
    releases = musicbrainzngs.browse_releases(
        label=mbid,
        release_status=['official'],
        release_type=['album', 'single', 'ep'],
        limit=limit,
        offset=offset
    )
    return releases


def parse_release_date(release):
    """Release dates in MusicBrainz don't have a standardized format. Some
    include the year, month, and date, some only a year, and others only
    a year and month. In some cases, releases don't have a date at all. This
    function handles the complexity of converting a release's date to a python
    datetime object."""
    try:
        date_string = release['date']
        if len(date_string) == 4:    # month and date missing
            date_string += "-01-01"
        elif len(date_string) == 7:  # date missing
            date_string += "-01"
        return datetime.strptime(date_string, "%Y-%m-%d")
    except KeyError:  # no date specified
        return None
    except ValueError:  # invalid date format
        return None


def clean_string(string):
    """Clean a string by trimming leading and trailing whitespace and
    converting to ASCII."""
    return ascii(string.strip())[1:-1]


def get_image_path(obj_type):
    """Get an image path for an object of type obj_type (album, artist)."""
    path = IMAGE_DIR + obj_type + "/"
    path += str(int(time.time() + random.random()))
    path += ".jpg"
    return path


def get_song_length(track):
    """Get the length of a particular track (ms)."""
    if 'length' in track:
        return int(track['length'])
    elif 'length' in track['recording']:
        return int(track['recording']['length'])
    else:
        return 0


def save_album_image(release_id):
    output_path = get_image_path('album')

    while True:
        try:
            data = musicbrainzngs.get_image_front(release_id,
                                                  size="500")
            with open(output_path, 'wb') as f:
                f.write(data)
            image_id, inserted = safe_insert('image', release_id, {
                'path': output_path,
                'size': 'CATALOG'
            })
            return image_id
        except ResponseError as err:
            if err.cause.code == 404:
                break  # image doesn't exist
        except NetworkError:
            break


def save_featured_artists(song_id, recording):
    """Save the featured artists for the given recording. Any artist other than
    the first credited artist is considered a featured artist."""
    for artist_credit in recording['artist-credit'][1:]:
        try:
            save_artist(artist_credit['artist'])
        except TypeError:
            # skip strings like '&' and 'feat.'
            continue


def save_genres(song_id, recording):
    """Save the genres for a song by finding the tags associated with its
    recording."""
    if 'tag-list' not in recording:
        return
    for tag in recording['tag-list']:
        if int(tag['count']) == 0:  # tag not applied to recording
            continue
        genre_id, inserted = safe_insert('genre', tag['name'], tag)
        insert('song_genre', (song_id, genre_id))


def save_album_songs(label_id, album_id, image_id, tracklist):
    """Save songs in tracklist & associate them with a label and album. Since
    original track numbers might contain non-numeric characters, we assign
    sequential integer track numbers to each track."""
    # todo: save lyrics
    for idx, track in enumerate(tracklist):
        length = get_song_length(track)
        song_id = insert('song', {
            'active': length > 0,  # disable songs with unknown length
            'name': track['recording']['title'],
            'mbid': track['id'],
            'length': length,
            'play_count': 0,
            'track_number': idx + 1,
            'album_id': album_id,
            'image_id': image_id   # same as album image
        })
        insert('album_song', (album_id, song_id))
        insert('song_label', (song_id, label_id))
        save_featured_artists(song_id, track['recording'])
        save_genres(song_id, track['recording'])


# DATA INSERTION ROUTINES

def saved(etype, key):
    """Check whether an entity with the type `etype` and key `key` has been
    saved."""
    if etype not in saved_entities:
        return False
    return key in saved_entities[etype]


def insert(etype, data):
    """Insert an entity of type `etype` into the database."""
    # clean string values in dictionaries
    if isinstance(data, dict):
        for key, val in data.items():
            if isinstance(val, str):
                data[key] = clean_string(val)
    cursor.execute(getattr(queries, "insert_" + etype), data)
    return cursor.lastrowid


def safe_insert(etype, key, data):
    """Insert an entity of type `etype` with key `key` into the database,
    ensuring that the key is unique. Return a tuple (row_id, saved) indicating
    the internal id of the entity and whether it was saved or not."""
    global saved_entities
    # clean string keys
    if isinstance(key, str):
        key = clean_string(key)
    # insert if not saved already
    saved_already = saved(etype, key)
    if not saved_already:
        if etype not in saved_entities:
            saved_entities[etype] = {}
        saved_entities[etype][key] = insert(etype, data)
    return (saved_entities[etype][key], not saved_already)


def save_artist(artist):
    # todo: find and save cover image (google) & bio (wikipedia)
    to_save = (artist['name'], artist['id'])
    artist_id, inserted = safe_insert('artist', artist['id'], to_save)
    if inserted and 'alias-list' in artist:
        for alias in artist['alias-list']:
            insert('artist_alias', (artist_id, alias['alias']))
    return artist_id


# IMPORT ROUTINES

def import_release(label_id, release):
    try:
        release = musicbrainzngs.get_release_by_id(
            release['id'],
            includes=RELEASE_EXTRAS
        )['release']
    except IndexError as err:
        print('skipping release {}: invalid response.')
        return

    medium = release['medium-list'][0]

    # save artist
    artist_id = save_artist(release['artist-credit'][0]['artist'])

    # save image
    image_id = None
    if release['cover-art-archive']['front'] != 'false':
        image_id = save_album_image(release['id'])

    # save album & songs
    album_id, inserted = safe_insert('album', release['id'], {
        'name': release['title'],
        'release_date': parse_release_date(release),
        'music_brainz_id': release['id'],
        'num_songs': medium['track-count'],
        'image_id': image_id,
        'artist_id': artist_id,
        'duration': 0   # updated after songs are imported
    })
    if inserted:
        save_album_songs(label_id, album_id, image_id, medium['track-list'])
        insert('artist_album', (artist_id, album_id))
        print('inserted album {} with {} songs.'
              .format(release['id'], medium['track-count']), flush=True)
    else:
        print('skipping release {}: already imported.'.format(release['id']))


def import_label(name, mbid):
    releases = get_label_releases(mbid, 0, 1)
    num_releases = releases['release-count']
    selected = random.sample(range(num_releases - RELEASES_PER_PAGE),
                             ceil(RELEASES_PER_LABEL / RELEASES_PER_PAGE))
    label_id = insert('label', {
        'name': name,
        'music_brainz_id': mbid
    })
    for offset in selected:
        batch = get_label_releases(mbid, offset, RELEASES_PER_PAGE)
        for release in batch['release-list']:
            import_release(label_id, release)
    conn.commit()


# main method
def do_import():
    global conn, cursor

    musicbrainzngs.set_useragent("SBUIfy", "0.0.1", "sbuify@gmail.com")

    conn = connector.connect(user=DB_USER, host=DB_HOST, database=DB_NAME,
                             password=DB_PASS)
    cursor = conn.cursor()

    # remove existing data
    print('deleting existing data...')

    t_start = time.time()
    with open(TRUNCATE_SQL, 'r') as f:
        statements = f.read().split(';')
        for statement in statements:
            if statement:
                cursor.execute(statement)
        conn.commit()

    for obj_type in ['album', 'artist']:
        images_dir = IMAGE_DIR + obj_type
        if os.path.exists(images_dir):
            shutil.rmtree(images_dir)
        os.makedirs(images_dir)
    t_end = time.time()

    print('data deleted in {:.3f} seconds'.format(t_end - t_start), flush=True)

    # import labels & all associated albums, songs, and artists
    print('importing data...', flush=True)

    for name, mbid in LABELS.items():
        t_start = time.time()
        import_label(name, mbid)
        t_end = time.time()

        print('imported label {} in {:.3f} seconds'
              .format(name, t_end - t_start), flush=True)

    # clean up
    cursor.close()
    conn.close()


do_import()
