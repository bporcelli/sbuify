"""Image processing module."""
from PIL import Image
import serpscrap
import uuid
import urllib
import requests
import io
import db


db.init()


SRV_DIR = '../server/'
IMG_DIR = 'static/img/'
OUT_DIR = SRV_DIR + IMG_DIR
IMG_EXT = '.jpg'
NUM_RES = 25
IM_SIZES = {
    'catalog': (500, 500),
    'cover': (1600, 375),
    'primary': (1134, 289),
    'thumbnail': (164, 164)
}


def save(data, size):
    """Save an image described by `data` in the size `size.`
    Return the ID assigned to the image, or None on failure."""
    data = io.BytesIO(data)
    img = Image.open(data)
    dims = IM_SIZES.get(size, None)
    w, h = img.size

    if dims is None:  # invalid size
        return None
    if w < dims[0] or h < dims[1]:  # image too small
        return None

    image_id = _save_orig(img)
    cropped = crop(img, dims[0], dims[1])
    _save_size(cropped, image_id, size)
    return image_id


def _save_orig(img):
    """Save an image."""
    filename = _save(img)
    image_id = db.save('image', {
        'width': img.size[0],
        'height': img.size[1]
    }, filename)
    db.save('image_size', {
        'image_id': image_id,
        'size': 'FULL',
        'path': IMG_DIR + filename
    }, (image_id, 'full'))
    return image_id


def _save_size(img, image_id, size):
    """Save the image identified by `image_id` in size `size.`"""
    filename = _save(img)
    db.save('image_size', {
        'image_id': image_id,
        'size': size.upper(),
        'path': IMG_DIR + filename
    }, (image_id, size))


def _save(img):
    """Save an image with a randomly generated filename and return
    its name."""
    filename = str(uuid.uuid4()) + IMG_EXT
    path = OUT_DIR + filename
    img.save(path)
    return filename


def crop(img, w, h):
    """Crop Image `img` to width `w` and height `h`."""
    image_w = img.size[0]
    image_h = img.size[1]

    if w > h or image_h > image_w:
        image_h = int((image_h / image_w) * w)
        image_w = w
    else:
        image_w = int((image_w / image_h) * h)
        image_h = h

    img = img.resize((image_w, image_h))

    if image_w == w and image_h == h:
        return img
    elif image_w != w:
        half_w = image_w / 2
        box = (half_w - (w / 2), 0, half_w + (w / 2), h)
    else:
        half_h = image_h / 2
        box = (0, half_h - (h / 2), w, half_h + (h / 2))
    return img.crop(box)


def save_artist_image(aname, size):
    """Find and save an image for the artist with name `aname`."""
    # todo: save both images with one call
    config = serpscrap.Config()

    config.set('search_engines', ['googleimg'])
    config.set('pages_per_keyword', 1)
    config.set('screenshot', False)
    config.set('search_type', 'image')
    config.set('sleeping_max', 10)
    config.set('image_type', 'any')  # required -- has no effect
    config.set('image_size', 'l')  # required -- has no effect

    keywords = [aname]
    scrap = serpscrap.SerpScrap()
    scrap.init(keywords=keywords, config=config.get())
    results = scrap.run()

    # limit to first NUM_RES results
    if len(results) > NUM_RES:
        results = results[:NUM_RES]

    for result in results:
        url = urllib.parse.unquote(result['serp_url'])
        try:
            content = requests.get(url).content
            saved = save(content, size)
            if saved is not None:
                return saved
        except Exception as e:
            pass
    return None


if __name__ == "__main__":
    image_id = save_artist_image("John Denver", 'cover')

    db.execute("DELETE FROM image_sizes WHERE image_id = {}".format(image_id))
    db.execute("DELETE FROM image WHERE id = {}".format(image_id))

    image_id = save_artist_image("John Denver", 'catalog')

    db.execute("DELETE FROM image_sizes WHERE image_id = {}".format(image_id))
    db.execute("DELETE FROM image WHERE id = {}".format(image_id))

db.close()
