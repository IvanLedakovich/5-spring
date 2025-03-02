package com.ivanledakovich.logic;

import com.ivanledakovich.database.FileRepository;
import com.ivanledakovich.models.FileModel;
import com.ivanledakovich.utils.TextFileNamingSystem;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    private final FileRepository fileRepository;
    private final ExecutorService executor;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    private Future<File> submit(File textFile, String imageType, String saveLocation) {
        return executor.submit(
                new FileProcessor(textFile, imageType, saveLocation, fileRepository)
        );
    }

    @Override
    public Future<File> processMultipart(MultipartFile file, String imageType, String saveLocation) throws IOException {
        File textFile = createTextFileFromMultipart(file);
        return submit(textFile, imageType, saveLocation);
    }

    @Override
    public List<Future<File>> process(List<String> filePaths, String imageType, String saveLocation) {
        return filePaths.stream()
                .map(path -> submit(new File(path), imageType, saveLocation))
                .toList();
    }

    private File createTextFileFromMultipart(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        String uniqueName = TextFileNamingSystem.generateUniqueTextFileName(originalName);

        File textFile = File.createTempFile(
                FilenameUtils.getBaseName(uniqueName),
                "." + FilenameUtils.getExtension(uniqueName)
        );
        file.transferTo(textFile);
        return textFile;
    }

    @Override
    public void awaitCompletion(List<Future<File>> futures) {
        for (Future<File> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("File processing failed: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }

    @Override
    public FileModel getFileByName(String fileName) {
        return fileRepository.findByFileNameOrImageName(fileName);
    }

    @Override
    public List<FileModel> getAllFiles() {
        return fileRepository.getAllFiles();
    }
}