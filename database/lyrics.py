from azlyrics import Azlyrics
from azlyrics.cache import Cache

# lyrics cache
cache = Cache()


def to_ascii(string):
    """Convert a string to ascii encoding, replacing invalid
    characters with XML character references."""
    encoded = string.encode('ascii', errors='xmlcharrefreplace')
    return encoded.decode('ascii')


def get_lyrics(track):
    """Get the lyrics for a track using the AzLyrics API. If no
    lyrics are available, return an empty string."""
    credit = track['recording']['artist-credit'][0]
    artist = credit['artist']['name']
    song = track['recording']['title']

    az = Azlyrics(artist, song)
    cache_key = '_'.join(az.normalize_artist_music())
    lyrics = cache.get(cache_key)

    if lyrics is None:
        try:
            lyrics = to_ascii(az.format_lyrics(az.get_lyrics()))
        except Exception:
            lyrics = ""  # lyrics not available
        cache.add(cache_key, lyrics)
    return lyrics
