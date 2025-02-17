package com.ivanledakovich.utils;

import org.apache.commons.io.FilenameUtils;
import java.util.UUID;

public class TextFileNamingSystem {
    public static String generateUniqueTextFileName(String originalFilename) {
        String baseName = FilenameUtils.getBaseName(originalFilename);
        String extension = FilenameUtils.getExtension(originalFilename);
        return baseName + "-" + UUID.randomUUID() + "." + extension;
    }
}