package com.ivanledakovich;

import com.ivanledakovich.models.FileModel;
import com.ivanledakovich.services.StorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(args = {
        "--file-type=png",
        "--save-location=target/test-output",
        "--file-path=src/test/resources/test.txt"
})
@TestPropertySource(properties = "app.storage.type=filesystem")
class ApplicationCLIIntegrationTest {

    @Autowired
    private StorageService storageService;

    @Test
    void testCliProcessing() throws Exception {
        List<FileModel> files = storageService.getAllFiles();
        assertFalse(files.isEmpty());
        files.forEach(file -> {
            assertNotNull(file.getFileName());
            assertNotNull(file.getImageName());
            assertTrue(file.getFileName().endsWith(".txt"));
            assertTrue(file.getImageName().endsWith(".png"));
        });
    }
}