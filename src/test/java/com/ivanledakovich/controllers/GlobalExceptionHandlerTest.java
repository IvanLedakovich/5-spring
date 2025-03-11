//package com.ivanledakovich.controllers;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class GlobalExceptionHandlerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void testStorageExceptionHandling() throws Exception {
//        mockMvc.perform(get("/download")
//                        .param("fileName", "nonexistent.txt"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void testGenericExceptionHandling() throws Exception {
//        mockMvc.perform(get("/invalid-endpoint"))
//                .andExpect(status().is4xxClientError())
//                .andExpect(view().name("error"));
//    }
//}