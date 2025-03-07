//package com.ivanledakovich.controllers;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.io.TempDir;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//
//import java.nio.file.Path;
//
//@SpringBootTest
//@ActiveProfiles("filesystem")
//@TestPropertySource(properties = {
//        "app.storage.type=filesystem",
//        "app.storage.path=${java.io.tmpdir}/filestorage"
//})
//class FileSystemFileControllerIntegrationTest extends AbstractFileControllerIntegrationTest {
//
//    @TempDir
//    static Path tempStoragePath;
//
//    @Test
//    void testFileLifecycle() throws Exception {
//        testFileUploadAndDownload();
//    }
//}