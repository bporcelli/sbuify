from util import strip_quotes, clean_string


# genres
GENRES = [
    {
        'id': 836,
        'name': 'A Cappella',
        'aliases': ['a cappella', 'doo wop']
    },
    {
        'id': 837,
        'name': 'Acoustic',
        'aliases': ['acoustic']
    },
    {
        'id': 838,
        'name': 'Alternative',
        'aliases': ['alternative', 'grunge', 'post grunge',
                    'post punk', 'seattle']
    },
    {
        'id': 839,
        'name': 'Big Band',
        'aliases': ['big band']
    },
    {
        'id': 840,
        'name': 'Blues',
        'aliases': ['blues']
    },
    {
        'id': 841,
        'name': 'Chill',
        'aliases': ['chill', 'chillout', 'dreamy', 'ambient',
                    'downtempo', 'lo-fi', 'mellow', 'trip hop']
    },
    {
        'id': 842,
        'name': 'Christian',
        'aliases': ['christian', 'gospel', 'religious']
    },
    {
        'id': 843,
        'name': 'Classical',
        'aliases': ['classical', 'piano', 'symphony']
    },
    {
        'id': 844,
        'name': 'Comedy',
        'aliases': ['comedy', 'humour', 'sketch comedy']
    },
    {
        'id': 845,
        'name': 'Country',
        'aliases': ['country', 'honky tonk', 'bluegrass']
    },
    {
        'id': 846,
        'name': 'Dance',
        'aliases': ['dance', 'boogie', 'breakbeat', 'disco',
                    'jive', 'salsa', 'samba', 'swing'],
    },
    {
        'id': 847,
        'name': 'Electronic',
        'aliases': ['electronic', 'dubstep', 'club', 'house',
                    'electro', 'lounge', 'techno', 'trance']
    },
    {
        'id': 848,
        'name': 'Easy Listening',
        'aliases': ['easy listening']
    },
    {
        'id': 849,
        'name': 'Experimental',
        'aliases': ['experimental', 'avant garde']
    },
    {
        'id': 850,
        'name': 'Funk',
        'aliases': ['funk']
    },
    {
        'id': 851,
        'name': 'Hip Hop',
        'aliases': ['hip hop', 'boom bap', 'gangsta', 'hardcore',
                    'horrorcore', 'urban', 'rap']
    },
    {
        'id': 852,
        'name': 'Holiday',
        'aliases': ['holiday', 'christmas', 'halloween']
    },
    {
        'id': 853,
        'name': 'Indie',
        'aliases': ['indie']
    },
    {
        'id': 854,
        'name': 'Instrumental',
        'aliases': ['instrumental']
    },
    {
        'id': 855,
        'name': 'Jazz',
        'aliases': ['jazz', 'smooth jazz']
    },
    {
        'id': 856,
        'name': 'Metal',
        'aliases': ['metal', 'hair metal', 'black metal',
                    'death metal']
    },
    {
        'id': 857,
        'name': 'Oldies',
        'aliases': ['oldies', '40s', '50s']
    },
    {
        'id': 858,
        'name': 'Other',
        'aliases': ['other', 'misc', 'miscellaneous', 'none',
                    'novelty', 'unclassifiable', 'unknown']
    },
    {
        'id': 859,
        'name': 'Party',
        'aliases': ['party']
    },
    {
        'id': 860,
        'name': 'Word',
        'aliases': ['word', 'poetry', 'spoken word']
    },
    {
        'id': 861,
        'name': 'Pop',
        'aliases': ['pop', 'pop/rock', 'synthpop']
    },
    {
        'id': 862,
        'name': 'Psychedelic',
        'aliases': ['psychedelic', 'psychadelic']
    },
    {
        'id': 863,
        'name': 'Punk',
        'aliases': ['punk']
    },
    {
        'id': 864,
        'name': 'R&B',
        'aliases': ['r&b', 'rnb', 'rhythm & blues',
                    'rhythm and blues', 'rhythm blues']
    },
    {
        'id': 865,
        'name': 'Reggae',
        'aliases': ['reggae', 'roots reggae']
    },
    {
        'id': 866,
        'name': 'Rock',
        'aliases': ['rock', 'new wave', 'progressive', 'classic rock',
                    'progressive rock', 'rock & roll', 'rock and roll',
                    'rockabilly', 'surf']
    },
    {
        'id': 867,
        'name': 'Roots & Folk',
        'aliases': ['roots & folk', 'bluegrass', 'roots rock']
    },
    {
        'id': 868,
        'name': 'Show Tunes',
        'aliases': ['show tunes', 'musical', 'stage & screen']
    },
    {
        'id': 869,
        'name': 'Soul',
        'aliases': ['soul', 'smooth soul', 'neo soul', 'neo-soul']
    }
]

# id of 'Other' genre
OTHER = 858


def find(tag_name):
    """Find the genre matching the given tag name, if any."""
    name = strip_quotes(clean_string(tag_name))
    genre_id = -1
    for genre in GENRES:
        if name in genre['aliases']:
            genre_id = genre['id']
            break
    return genre_id
