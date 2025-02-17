package com.ivanledakovich.controllers;

import com.ivanledakovich.logic.FileReader;
import com.ivanledakovich.logic.FileService;
import com.ivanledakovich.logic.ImageCreator;
import com.ivanledakovich.models.FileModel;
import com.ivanledakovich.utils.ImageFileNamingSystem;
import com.ivanledakovich.utils.TextFileNamingSystem;
import org.apache.commons.io.FilenameUtils;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

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

        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            String uniqueTextFileName = TextFileNamingSystem.generateUniqueTextFileName(originalFilename);
            String uniqueImageFileName = ImageFileNamingSystem.generateUniqueImageName(originalFilename, imageExtension);

            File txtFile = File.createTempFile(
                    FilenameUtils.getBaseName(uniqueTextFileName),
                    "." + FilenameUtils.getExtension(uniqueTextFileName)
            );

            file.transferTo(txtFile);

            String data = FileReader.readFile(txtFile.getAbsolutePath());
            BufferedImage image = ImageCreator.createImage(data);


            File imageFile = new File(saveLocation, uniqueImageFileName);
            ImageIO.write(image, imageExtension, imageFile);

            fileService.insertAFile(txtFile, imageFile);
        }
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