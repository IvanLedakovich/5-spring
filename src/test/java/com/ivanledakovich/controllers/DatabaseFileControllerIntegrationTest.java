//package com.ivanledakovich.controllers;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//
//@SpringBootTest
//@ActiveProfiles("database")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(properties = {
//        "app.storage.type=database",
//        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
//        "spring.datasource.driver-class-name=org.h2.Driver",
//        "spring.jpa.hibernate.ddl-auto=create-drop"
//})
//class DatabaseFileControllerIntegrationTest extends AbstractFileControllerIntegrationTest {
//
//    @Test
//    void testFileLifecycle() throws Exception {
//        testFileUploadAndDownload();
//    }
//}