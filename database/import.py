from math import ceil
from datetime import datetime
from musicbrainzngs.musicbrainz import ResponseError
from musicbrainzngs.musicbrainz import NetworkError
import genres
import musicbrainzngs
import lyrics
import db
import random
import time
import shutil
import os


# CONFIG
REL_PER_LABEL = 50  # releases per label
REQ_PER_LABEL = 5   # requests per label
REL_PER_REQ = ceil(REL_PER_LABEL / REQ_PER_LABEL)
RETRY_SECS = 5
CONTENT_DIR = '../content/'
IMAGE_DIR = 'images/'


# HELPERS

def delete_images():
    """Remove album & artist images from the previous import."""
    for obj_type in ['album', 'artist']:
        images_dir = CONTENT_DIR + IMAGE_DIR + obj_type
        if os.path.exists(images_dir):
            shutil.rmtree(images_dir)
        os.makedirs(images_dir)


def get_filename():
    """Generate a filename for a new image."""
    return str(int(time.time() + random.random())) + ".jpg"


def get_label_releases(label_id, offset, limit):
    """Get up to `limit` releases for label `label_id` starting from
    offset `offset`."""
    releases = musicbrainzngs.browse_releases(
        label=label_id,
        release_status=['official'],
        release_type=['album', 'single', 'ep'],
        includes=['release-groups'],
        limit=limit,
        offset=offset
    )
    return releases


def get_release(release_id):
    """Use the MusicBrainz API to get the release with id `id`. If
    the API response is invalid, return None; otherwise, return the
    release group as a dictionary."""
    try:
        return musicbrainzngs.get_release_by_id(
            release_id,
            includes=['artists', 'artist-credits', 'aliases', 'tags',
                      'recordings', 'media', 'release-groups']
        )['release']
    except IndexError as err:
        return None


def parse_release_date(release):
    """Convert an album release date into a Python datetime object,
    accounting for missing or malformatted data."""
    try:
        date_string = release['date']
        if len(date_string) == 4:    # month and date missing
            date_string += "-01-01"
        elif len(date_string) == 7:  # date missing
            date_string += "-01"
        return datetime.strptime(date_string, "%Y-%m-%d")
    except KeyError:    # no date specified
        return None
    except ValueError:  # invalid date format
        return None


def get_song_length(track):
    """Given the song `track`, return its length in milliseconds.
    If the length is unspecified, return 0."""
    if 'length' in track:
        return int(track['length'])
    elif 'length' in track['recording']:
        return int(track['recording']['length'])
    else:
        return 0


# DATA INSERTION ROUTINES

def save_genres(song_id, recording):
    """Save the tags for recording `recording` as genres for song
    `song_id`."""
    song_genres = []

    if 'tag-list' in recording:
        for tag in recording['tag-list']:
            if int(tag['count']) == 0:  # tag not applied
                continue
            genre_id = genres.find(tag['name'])
            if genre_id != -1 and genre_id not in song_genres:
                db.save('song_genre', (song_id, genre_id))
                song_genres.append(genre_id)

    # put song in 'Other' category if no genres were found
    if len(song_genres) == 0:
        db.save('song_genre', (song_id, genres.OTHER))


def save_cover_art(release, max_retries=3):  # todo: discard low-res
    """Save the front cover image for the release `release`. Return
    the saved image's ID, or None if the image isn't available."""
    if release['cover-art-archive']['front'] == 'false':
        return None

    rel_path = IMAGE_DIR + "album/" + get_filename()
    abs_path = CONTENT_DIR + rel_path

    for retry in range(max_retries):
        try:
            image_data = musicbrainzngs.get_image_front(release['id'],
                                                        size="500")
            with open(abs_path, 'wb') as f:
                f.write(image_data)
            return db.save('image', {
                'path': rel_path,
            }, release['release-group']['id'])
        except ResponseError as err:
            if err.cause.code == 404:    # image doesn't exist
                break
            elif err.cause.code == 503:  # rate limit exceeded
                print('rate limit exceeded: retrying in {} seconds.'
                      .format(RETRY_SECS))
                time.sleep(RETRY_SECS)
        except NetworkError:
            break


def save_track(track, album_id, image_id):
    """Save track `track` from album `album_id` with image
    `image_id`. Return the saved song's ID."""
    song = {
        'name': track['recording']['title'],
        'mbid': track['id'],
        'length': get_song_length(track),
        'track_number': track['number'],
        'album_id': album_id,
        'image_id': image_id,
        'lyrics': lyrics.get_lyrics(track)
    }
    song['active'] = song['length'] > 0

    song_id = db.save('song', song)

    featured = track['recording']['artist-credit'][1:]
    for artist in featured:
        if isinstance(artist, str):
            continue  # skip strings like '&' and 'feat.'
        artist_id = save_artist(artist['artist'])
        db.save('song_featured_artists', (song_id, artist_id))

    save_genres(song_id, track['recording'])
    return song_id


def save_album(release, label_id, artist_id):
    """Save album `release` released on label `label_id` by artist
    `artist_id`."""
    image_id = save_cover_art(release)

    album_id = db.save('album', {
        'name': release['title'],
        'mbid': release['id'],
        'type': release['release-group']['primary-type'].upper(),
        'release_date': parse_release_date(release),
        'image_id': image_id,
        'artist_id': artist_id,
    }, release['release-group']['id'])

    medium = release['medium-list'][0]

    for ti, track in enumerate(medium['track-list']):
        track['number'] = ti + 1
        song_id = save_track(track, album_id, image_id)
        db.save('album_song', (album_id, song_id))
        db.save('label_song', (label_id, song_id))

    db.save('artist_album', (artist_id, album_id))

    print('inserted album {} with {} songs.'
          .format(release['id'], medium['track-count']), flush=True)


def save_artist(artist):  # todo: cover image & bio
    """Save an artist `artist` and return the artist's ID."""
    saved_id = db.get_saved_id('artist', artist['id'])
    if saved_id:
        return saved_id
    to_save = (artist['name'], artist['id'])
    saved_id = db.save('artist', to_save, to_save[1])
    if 'alias-list' in artist:
        for alias in artist['alias-list']:
            db.save('artist_alias', (saved_id, alias['alias']))
    return saved_id


# IMPORT ROUTINES

def import_release(label_id, release):
    """Import release `release` for label `label_id`. Ensure that
    only one release from each release group is imported."""
    group_id = release['release-group']['id']

    if db.get_saved_id('album', group_id):  # already imported
        print('skipping release {}: already imported.'
              .format(group_id))
        return

    release = get_release(release['id'])

    if release:
        artist_id = save_artist(release['artist-credit'][0]['artist'])
        save_album(release, label_id, artist_id)
    else:
        print('skipping release {}: invalid response'
              .format(group_id))


def import_label_releases(label_id, mbid):
    """Import `REL_PER_LABEL` releases for the label with ID
    `label_id` and MB id `mbid`, including all associated songs
    and artists."""
    releases = get_label_releases(mbid, 0, 1)
    sample_range = range(releases['release-count'] - REL_PER_REQ)
    imported = []

    for req_i in range(REQ_PER_LABEL):  # todo: ensure exactly REL_PER
        start = random.choice(sample_range)
        while start in imported:
            start = random.choice(sample_range)
        batch = get_label_releases(mbid, start, REL_PER_REQ)
        for release in batch['release-list']:
            import_release(label_id, release)
        imported += list(range(start, start + REL_PER_REQ + 1))


def do_import():
    musicbrainzngs.set_useragent("SBUIfy", "0.0.1", "sbuify@gmail.com")

    db.init()

    # remove existing data
    print('deleting existing data...')
    delete_images()
    db.execute_script('truncate.sql')
    print('done.', flush=True)

    # import albums, songs, and artists for all labels
    db.execute("SELECT * FROM label")

    for label in db.get_cursor().fetchall():
        print('importing label {}...'.format(label['name']))
        import_label_releases(label['id'], label['mbid'])
        print('done.', flush=True)

    # wrap up
    print('finishing...')
    db.execute_script('set_hibernate_sequence.sql')
    print('done.')

    db.close()


do_import()
