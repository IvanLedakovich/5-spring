package com.ivanledakovich.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorNotifier {
    private static final Logger logger = LoggerFactory.getLogger(ErrorNotifier.class);

    public static void invalidInputFilesNotification() {
        logger.error("Please check if the input file(s) are valid, of .txt format and not empty.");
    }

    public static void invalidFileTypeNotification() {
        logger.error("Please check if the selected image format is correct. The program accepts png and jpg format selection.");
    }
}
