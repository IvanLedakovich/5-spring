package com.ivanledakovich.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public abstract class AbstractFileControllerIntegrationTest {

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    protected void testFileUploadAndDownload() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "files",
                "test.txt",
                "text/plain",
                "Test content".getBytes()
        );

        // Test upload
        mockMvc.perform(multipart("/upload")
                        .file(file)
                        .param("imageExtension", "png")
                        .param("saveLocation", "default"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/files"));

        // Test listing
        mockMvc.perform(get("/files"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("files"));

        // Test download
        mockMvc.perform(get("/download")
                        .param("fileName", "test.txt"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"test.txt\""))
                .andExpect(content().bytes("Test content".getBytes()));
    }
}