package com.ivanledakovich.logic;

import com.ivanledakovich.database.FileRepository;
import com.ivanledakovich.utils.ImageFileNamingSystem;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

public class FileProcessor implements Callable<File> {
    private File textFile;
    private String imageType;
    private String saveLocation;
    private FileRepository fileRepository;

    public FileProcessor(File textFile, String imageType,
                                   String saveLocation, FileRepository fileRepository) {
        this.textFile = textFile;
        this.imageType = imageType;
        this.saveLocation = saveLocation;
        this.fileRepository = fileRepository;
    }

    @Override
    public File call() throws Exception {
        String originalName = textFile.getName();
        String uniqueImageName = ImageFileNamingSystem.generateUniqueImageName(originalName, imageType);

        BufferedImage image = ImageCreator.createImage(FileReader.readFile(textFile.getAbsolutePath()));
        File imageFile = saveImageFile(image, uniqueImageName);

        fileRepository.insertAFile(textFile, imageFile);
        return imageFile;
    }

    private File saveImageFile(BufferedImage image, String fileName) throws Exception {
        File outputDir = new File(saveLocation);
        if (!outputDir.exists()) outputDir.mkdirs();

        File imageFile = new File(outputDir, fileName);
        if (!ImageIO.write(image, imageType, imageFile)) {
            throw new IOException("Failed to write image file");
        }
        return imageFile;
    }
}