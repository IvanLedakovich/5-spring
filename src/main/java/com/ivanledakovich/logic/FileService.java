package com.ivanledakovich.logic;

import com.ivanledakovich.database.FileRepository;
import com.ivanledakovich.models.FileModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public void insertAFile(File txtFile, File imageFile) throws IOException {
        fileRepository.insertAFile(txtFile, imageFile);
    }

    public FileModel getFileByName(String fileName) {
        return fileRepository.findByFileNameOrImageName(fileName);
    }

    public List<FileModel> getAllFiles() {
        return fileRepository.getAllFiles();
    }
}