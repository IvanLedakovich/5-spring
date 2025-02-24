package com.ivanledakovich.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class FileProcessor implements Runnable {

    private final FileService fileService;
    private String imageFileType;
    private String imageSaveLocation;
    private String textFilePath;

    @Autowired
    public FileProcessor(FileService fileService) {
        this.fileService = fileService;
    }

    public void configure(String imageFileType, String imageSaveLocation, String textFilePath) {
        this.imageFileType = imageFileType;
        this.imageSaveLocation = imageSaveLocation;
        this.textFilePath = textFilePath;
    }

    @Override
    public void run() {
        File txtFile = new File(textFilePath);
        String imageName = txtFile.getName() + "." + imageFileType;
        File imageFile = new File(imageSaveLocation, imageName);
        String data = FileReader.readFile(textFilePath);
        BufferedImage image = ImageCreator.createImage(data);
        try {
            ImageIO.write(image, imageFileType, imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fileService.insertAFile(txtFile, imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        txtFile.deleteOnExit();
        imageFile.deleteOnExit();
    }
}