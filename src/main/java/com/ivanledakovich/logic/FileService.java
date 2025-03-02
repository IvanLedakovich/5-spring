package com.ivanledakovich.logic;

import com.ivanledakovich.models.FileModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

public interface FileService {
    Future<File> processFileInWebMode(MultipartFile file, String imageType, String saveLocation) throws IOException;
    List<Future<File>> processFilesInCLIMode(List<String> filePaths, String imageType, String saveLocation);
    void awaitCompletion(List<Future<File>> futures);
    void shutdown();
    FileModel getFileByName(String fileName);
    List<FileModel> getAllFiles();
}