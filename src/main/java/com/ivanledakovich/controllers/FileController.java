package com.ivanledakovich.controllers;

import com.ivanledakovich.logic.FileService;
import com.ivanledakovich.models.FileModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Controller handling file upload, download and listing operations.
 * Supports both database and filesystem storage through dependency injection.
 *
 * @author Ivan Ledakovich
 */
@Controller
public class FileController {
    private final FileService fileService;
    private final Map<Integer, String> saveLocations;

    public FileController(FileService fileService,
                          @Qualifier("saveLocations") Map<Integer, String> saveLocations) {
        this.fileService = fileService;
        this.saveLocations = saveLocations;
    }

    @GetMapping("/")
    public String uploadForm(Model model) {
        model.addAttribute("saveLocations", saveLocations);
        return "fileupload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("files") MultipartFile[] files,
                                   @RequestParam String imageExtension,
                                   @RequestParam String saveLocation) throws IOException {

        List<Future<File>> futures = new ArrayList<>();
        for (MultipartFile file : files) {
            futures.add(fileService.processMultipart(file, imageExtension, saveLocation));
        }

        fileService.awaitCompletion(futures);
        return "redirect:/files";
    }

    @GetMapping("/files")
    public String listFiles(Model model) {
        model.addAttribute("files", fileService.getAllFiles());
        return "allfiles";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) {
        FileModel fileModel = fileService.getFileByName(fileName);

        if (fileModel == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] fileData = fileName.equals(fileModel.getFileName()) ?
                fileModel.getFileData() : fileModel.getImageData();
        String contentType = fileName.equals(fileModel.getFileName()) ?
                "text/plain" : "image/" + fileModel.getImageType();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(new ByteArrayResource(fileData));
    }
}