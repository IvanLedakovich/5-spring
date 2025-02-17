package com.ivanledakovich.utils;

import org.apache.commons.io.FilenameUtils;
import java.util.UUID;

public class ImageFileNamingSystem {
    public static String generateUniqueImageName(String originalFilename, String extension) {
        String baseName = FilenameUtils.getBaseName(originalFilename);
        return baseName + "-" + UUID.randomUUID() + "." + extension;
    }
}