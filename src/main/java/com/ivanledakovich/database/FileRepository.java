package com.ivanledakovich.database;

import com.ivanledakovich.models.FileModel;
import java.io.File;
import java.util.List;

public interface FileRepository {
    FileModel findByFileNameOrImageName(String name);
    void insertAFile(File txtFile, File imageFile);
    List<FileModel> getAllFiles();
    void deleteFileByName(String name);
}