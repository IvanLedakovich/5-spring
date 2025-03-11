//package com.ivanledakovich.controllers;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@TestPropertySource(properties = {
//        "app.storage.type=mongodb",
//        "spring.data.mongodb.uri=mongodb://localhost:27017/testdb"
//})
//class MongoFileControllerIntegrationTest extends AbstractFileControllerIntegrationTest {
//
//    @Test
//    void testFileLifecycle() throws Exception {
//        testFileUploadAndDownload();
//    }
//}