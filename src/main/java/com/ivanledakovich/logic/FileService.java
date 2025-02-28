package com.ivanledakovich.logic;

import com.ivanledakovich.database.FileRepository;
import com.ivanledakovich.models.FileModel;
import com.ivanledakovich.utils.TextFileNamingSystem;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

/**
 * Service class handling file processing logic and async operations.
 * Manages thread pooling and coordinates between file processing and storage.
 *
 * @author Ivan Ledakovich
 */
@Service
public class FileService {
    private static final Logger logger = Logger.getLogger(FileService.class);
    private final FileRepository fileRepository;
    private final ExecutorService executor;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    private Future<File> submitTheFileForAsyncProcessingByAnExecutor(File textFile, String imageType, String saveLocation) {
        return executor.submit(
                new FileProcessor(textFile, imageType, saveLocation, fileRepository)
        );
    }

    public Future<File> processFileInWebMode(MultipartFile file, String imageType, String saveLocation)
            throws IOException {

        File textFile = createTextFileFromMultipart(file);
        return submitTheFileForAsyncProcessingByAnExecutor(textFile, imageType, saveLocation);
    }

    public List<Future<File>> processFilesInCLIMode(List<String> filePaths, String imageType, String saveLocation) {
        return filePaths.stream()
                .map(path -> submitTheFileForAsyncProcessingByAnExecutor(new File(path), imageType, saveLocation))
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

    public void awaitCompletion(List<Future<File>> futures) {
        for (Future<File> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("File processing failed: " + e.getMessage());
            }
        }
    }

    public void shutdown() {
        executor.shutdown();
    }

    public FileModel getFileByName(String fileName) {
        return fileRepository.findByFileNameOrImageName(fileName);
    }

    public List<FileModel> getAllFiles() {
        return fileRepository.getAllFiles();
    }
}