package com.cse308.sbuify.test;

import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.image.StorageException;
import com.cse308.sbuify.image.StorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class StorageServiceTest {

    private static final String TEST_IMAGE = "static/img/test-image.jpg";

    @Autowired
    private StorageService storageService;

    /**
     * Test: saving a base64 encoded image.
     */
    @Test
    public void save() throws IOException {
        Resource resource = storageService.loadAsResource(TEST_IMAGE);

        InputStream stream = resource.getInputStream();
        byte[] bytes = StreamUtils.copyToByteArray(stream);
        stream.close();

        String dataURL = "data:image/jpeg;base64," + Base64Utils.encodeToString(bytes);
        try {
            Image image = storageService.save(dataURL);

            assertEquals(1080, (long) image.getWidth());
            assertEquals(1048, (long) image.getHeight());
            assertNotNull(image.getPath());

            // delete all images
            String path = image.getPath();
            storageService.delete(image);
            assertFalse(Files.exists(Paths.get(path)));
        } catch (StorageException e) {
            fail("Failed to save image: " + e.getMessage());
        }
    }
}
