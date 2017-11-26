package com.cse308.sbuify.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static java.awt.Image.SCALE_SMOOTH;

/**
 * Service for saving images to the file system.
 */
@Service
public class StorageService {

    private static final String DATA_URL_PREFIX = "data:";

    private final Path rootDir;

    private final List<String> allowedFormats;

    private final Integer maxImageSize;

    @Autowired
    public StorageService(ImageProperties properties) {
        this.rootDir = Paths.get(properties.getDirectory());
        this.allowedFormats = properties.getAllowedFormats();
        this.maxImageSize = properties.getMaxImageSize();
    }

    /**
     * Save a base64 encoded image.
     *
     * @param dataURL Image as a base64 encoded data URI.
     * @return Image object.
     */
    public Image save(String dataURL) {
        dataURL = dataURL.replace(DATA_URL_PREFIX, "");

        // get image format from media type
        String mediaType = dataURL.substring(0, dataURL.lastIndexOf(';'));

        if (mediaType.isEmpty()) {
            throw new StorageException("Required media type is missing.");
        }
        String format = mediaType.substring(mediaType.indexOf("/") + 1);

        // decode & save image
        String data = dataURL.substring(dataURL.indexOf(',') + 1);
        InputStream stream = new ByteArrayInputStream(Base64Utils.decodeFromString(data));
        BufferedImage image;
        try {
            image = ImageIO.read(stream);
        } catch (IOException e) {
            throw new StorageException("Failed to decode image.", e);
        }
        return save(image, format);
    }

    /**
     * Save an image.
     *
     * @param src Full-sized image.
     * @param fmt Image format.
     * @return Image object.
     */
    private Image save(BufferedImage src, String fmt) {
        if (!isFormatAllowed(fmt)) {
            throw new StorageException("Invalid image format '" + fmt + "'.");
        }

        Image image = new Image();

        image.setWidth(src.getWidth());
        image.setHeight(src.getHeight());

        // save a copy of the image in all allowed sizes
        for (ImageSize size: ImageSize.values()) {
            if (src.getWidth() < size.width() || src.getHeight() < size.height()) {  // image too small
                continue;
            }
            java.awt.Image scaled = src.getScaledInstance(size.width(), size.height(), SCALE_SMOOTH);
            BufferedImage bufScaled = toBufferedImage(scaled);
            Path path = rootDir.resolve(UUID.randomUUID().toString());
            File file = path.toFile();
            try {
                ImageIO.write(bufScaled, fmt, file);
            } catch (IOException e) {
                throw new StorageException("Failed to save image.", e);
            }
            if (file.length() > maxImageSize) {
                file.delete();
                throw new StorageException("Image is too large.");
            }
            image.setPath(size, path.toString());
        }
        return image;
    }

    /**
     * Delete all files associated with an image.
     *
     * @param image The image to delete.
     */
    public void delete(Image image) {
        for (ImageSize size: ImageSize.values()) {
            String path = image.getPath(size);
            if (path == null) {
                continue;
            }
            try {
                Files.delete(Paths.get(path));
            } catch (IOException e) {
                throw new StorageException("Failed to delete image: " + path, e);
            }
        }
    }

    /**
     * Converts a java.awt.Image to a BufferedImage.
     */
    private BufferedImage toBufferedImage(java.awt.Image src) {
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        int type = BufferedImage.TYPE_INT_RGB;

        BufferedImage dest = new BufferedImage(width, height, type);

        Graphics2D graphics = dest.createGraphics();
        graphics.drawImage(src, 0, 0, null);
        graphics.dispose();

        return dest;
    }

    /**
     * Check whether the given image format is allowed.
     *
     * @param format Format as a String, e.g. "png".
     * @return Boolean indicating whether the format is allowed.
     */
    private boolean isFormatAllowed(String format) {
        return allowedFormats.contains(format);
    }

    /**
     * Load an image as a Resource.
     *
     * @param filename Filename.
     * @return Resource
     */
    public Resource loadAsResource(String filename) {
        Path path = rootDir.resolve(filename);
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            throw new StorageException("Failed to read file " + filename);
        } catch (MalformedURLException e) {
            throw new StorageException("Failed to read file " + filename, e);
        }
    }

    /**
     * Initialize the storage service.
     */
    public void init() {
        try {
            Files.createDirectories(rootDir);
        } catch (IOException e) {
            throw new StorageException("Failed to initialize image storage.", e);
        }
    }
}
