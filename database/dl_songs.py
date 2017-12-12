"""Download audio previews for all songs in the database."""
import youtube_dl
import uuid
import db


SONGS_DIR = "static/songs/"
OUT_DIR = "../server/" + SONGS_DIR
P_LENGTH = 30  # preview length (seconds)
ERR_SECS = 5   # allow song lengths to be off by 5 seconds


def video_filter(length):
    """Ignore livestreams and videos whose length differs from the
    current song by more than ERR_SECS seconds."""
    length_s = length / 1000

    def _filter(info_dict):
        flen = info_dict.get('duration')
        video_title = info_dict.get('title', info_dict.get(
                                    'id', 'video'))
        if info_dict.get('is_live') is not None:
            return '%s is a live stream, skipping ..' % (video_title)
        elif flen < length_s - ERR_SECS or flen > length_s + ERR_SECS:
            return '%s has the wrong length, skipping ..' % (video_title)
        return None

    return _filter


def download_songs():
    """Download high and low quality mp3 previews for all songs in
    the database."""
    db.init()

    db.execute("""
        SELECT DISTINCT CONCAT(ar.name, " - ", s.name) AS name, s.id AS id,
            s.length as length
        FROM song s, artist ar, album a
        WHERE s.album_id = a.id
            AND a.artist_id = ar.id
    """)

    dl_opts = {
        'format': 'best,worst',
        'postprocessors': [
            {
                'key': 'FFmpegExtractAudio',
                'preferredcodec': 'mp3',
                'preferredquality': '192',
            },
            {
                'key': 'ExecAfterDownload',
                'exec_cmd': 'ffmpeg -y -i {} -ss 0 -to ' + str(P_LENGTH) +
                            ' -map 0:0 temp.mp3'
            },
            {
                'key': 'ExecAfterDownload',
                'exec_cmd': 'mv temp.mp3 {}'
            }
        ],
        'default_search': 'ytsearch1:',
        'ignoreerrors': True
    }

    i_query = ("INSERT INTO `song_files` (`song_id`, `quality`, `path`)"
               " VALUES (%s, %s, %s)")

    for song in db.get_cursor().fetchall():
        for fmt in ['best', 'worst']:
            sid = str(uuid.uuid4())

            dl_opts['format'] = fmt
            dl_opts['outtmpl'] = OUT_DIR + sid + '.%(ext)s'
            dl_opts['match_filter'] = video_filter(song['length'])

            with youtube_dl.YoutubeDL(dl_opts) as ydl:
                ret = ydl.download([song['name']])
                if ret == 0:
                    data = (song['id'], fmt.upper(),
                            SONGS_DIR + sid + '.mp3')
                    db.execute(i_query, data)
                else:
                    print('failed to download {}: skipping.'.format(
                          song['name']))

    db.close()


download_songs()
