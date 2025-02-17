package com.ivanledakovich.services;

import com.ivanledakovich.database.FileRepository;
import com.ivanledakovich.models.FileModel;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.List;

@Service
@Profile("database")
public class DatabaseStorageService implements StorageService {

    private final FileRepository fileRepository;

    public DatabaseStorageService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public void insertAFile(File txtFile, File imageFile) {
        fileRepository.insertAFile(txtFile, imageFile);
    }

    @Override
    public FileModel getFileByName(String fileName) {
        return fileRepository.findByFileNameOrImageName(fileName);
    }

    @Override
    public List<FileModel> getAllFiles() {
        return fileRepository.getAllFiles();
    }

    @Override
    public void deleteFileByName(String fileName) {
        fileRepository.deleteFileByName(fileName);
    }
}