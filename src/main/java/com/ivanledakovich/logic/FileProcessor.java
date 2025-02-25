package com.ivanledakovich.logic;

import com.ivanledakovich.utils.ImageFileNamingSystem;
import com.ivanledakovich.utils.TextFileNamingSystem;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

@Component
public class FileProcessor implements Callable<File> {

    private String textFilePath;
    private String imageFileType;
    private String imageSaveLocation;

    public FileProcessor configure(String textFilePath, String imageFileType, String imageSaveLocation) {
        this.textFilePath = textFilePath;
        this.imageFileType = imageFileType;
        this.imageSaveLocation = imageSaveLocation;
        return this;
    }

    @Override
    public File call() throws Exception {
        File originalFile = new File(textFilePath);

        String uniqueTextName = TextFileNamingSystem.generateUniqueTextFileName(originalFile.getName());
        String uniqueImageName = ImageFileNamingSystem.generateUniqueImageName(originalFile.getName(), imageFileType);

        File processedTextFile = processTextFile(originalFile, uniqueTextName);
        File imageFile = generateImageFile(processedTextFile, uniqueImageName);

        return imageFile;
    }

    private File processTextFile(File originalFile, String uniqueName) throws IOException {
        File tempFile = File.createTempFile(
                FilenameUtils.getBaseName(uniqueName),
                "." + FilenameUtils.getExtension(uniqueName)
        );
        org.apache.commons.io.FileUtils.copyFile(originalFile, tempFile);
        return tempFile;
    }

    private File generateImageFile(File textFile, String imageName) throws IOException {
        String data = FileReader.readFile(textFile.getAbsolutePath());
        BufferedImage image = ImageCreator.createImage(data);

        File outputDir = new File(imageSaveLocation);
        if (!outputDir.exists()) outputDir.mkdirs();

        File imageFile = new File(outputDir, imageName);
        ImageIO.write(image, imageFileType, imageFile);
        return imageFile;
    }
}