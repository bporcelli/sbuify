from mysql import connector
from util import clean_string
import queries


# config
_SQL_DIR = './sql/'
_DB_USER = 'root'
_DB_PASS = '1234'
_DB_HOST = 'localhost'
_DB_NAME = 'test2'


# globals
_conn = None
_cursor = None
_saved_entities = {}  # map from mb keys to internal ids


def execute_script(filename):
    """Run an SQL script with the given filename."""
    with open(_SQL_DIR + filename, 'r') as f:
        statements = f.read().split(';')
        for statement in statements:
            if statement:
                execute(statement)
        _conn.commit()


def save(etype, data, key=None):
    """Save an entity `etype` with key `key` in the database using
    data `data`. If `data` is a dictionary, clean all string values
    to avoid issues with nonstandard character encodings. Return the
    ID of the saved entity."""
    global _saved_entities

    if isinstance(data, dict):
        if key is None:  # use value with key 'id' as key
            if 'id' in data:
                key = data['id']
            elif 'mbid' in data:
                key = data['mbid']
    if key is None:
        key = data

    query_string = getattr(queries, "insert_" + etype)
    saved_id = execute(query_string, data)
    if etype not in _saved_entities:
        _saved_entities[etype] = {}
    _saved_entities[etype][key] = saved_id

    return saved_id


def get_saved_id(etype, key):
    """If an entity with type `etype` and key `key` has been saved,
    return its id. Otherwise, return None."""
    global _saved_entities
    if etype not in _saved_entities:
        return None
    elif key not in _saved_entities[etype]:
        return None
    return _saved_entities[etype][key]


def get_cursor():
    """Return a reference to the DB cursor."""
    if _cursor is not None:
        return _cursor
    init()
    return _cursor


def execute(query, data=None):
    """Wrapper around MySQLCursor.execute"""
    _cursor.execute(query, data)
    return _cursor.lastrowid


def init():
    """Initialize the database connection & cursor."""
    global _cursor, _conn
    _conn = connector.connect(user=_DB_USER,
                              host=_DB_HOST,
                              database=_DB_NAME,
                              password=_DB_PASS)
    _cursor = _conn.cursor(dictionary=True)


def close():
    """Tear down the database connection."""
    _conn.commit()
    _cursor.close()
    _conn.close()
