package com.ivanledakovich.logic;

import com.ivanledakovich.database.FileRepository;
import com.ivanledakovich.models.FileModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class FileService {
    private static final Logger logger = Logger.getLogger(FileService.class);

    private final FileRepository fileRepository;
    private final ExecutorService executor;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public List<Future<File>> processFiles(List<String> textFilePaths, String imageType, String saveLocation) {
        List<Future<File>> futures = new ArrayList<>();

        for (String path : textFilePaths) {
            futures.add(executor.submit(
                    new FileProcessor().configure(path, imageType, saveLocation)
            ));
        }
        return futures;
    }

    public void awaitCompletion(List<Future<File>> futures) {
        for (Future<File> future : futures) {
            try {
                File result = future.get();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void shutdown() {
        executor.shutdown();
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