package com.ivanledakovich.services;

import com.ivanledakovich.models.FileModel;
import java.io.File;
import java.util.List;

public interface StorageService {
    void insertAFile(File txtFile, File imageFile) throws Exception;
    FileModel getFileByName(String fileName) throws Exception;
    List<FileModel> getAllFiles() throws Exception;
    void deleteFileByName(String fileName) throws Exception;
}