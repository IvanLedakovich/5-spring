package com.ivanledakovich.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.Callable;

@Component
public class FileProcessor implements Callable<Integer> {

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
    public Integer call() throws Exception {
        File txtFile = new File(textFilePath);
        String imageName = txtFile.getName() + "." + imageFileType;
        File imageFile = new File(imageSaveLocation, imageName);
        String data = FileReader.readFile(textFilePath);
        BufferedImage image = ImageCreator.createImage(data);
        ImageIO.write(image, imageFileType, imageFile);
        fileService.insertAFile(txtFile, imageFile);
        txtFile.deleteOnExit();
        imageFile.deleteOnExit();
        return 0;
    }
}