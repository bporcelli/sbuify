"""Import utilities."""


def strip_quotes(string):
    """Strip all quotes from the string `string`."""
    return string.replace('"', '').replace("'", '')


def clean_string(string):
    """Clean the string `string` by trimming excess whitespace and
    converting to ASCII encoding. `ascii` returns a quoted string,
    so we strip the quotes using the slicing operator.
    """
    return ascii(string.strip())[1:-1]
