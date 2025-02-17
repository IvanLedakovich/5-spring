package com.ivanledakovich.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanledakovich.models.FileModel;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileSystemRepository implements FileRepository {

    private final Path storagePath;
    private final Path metadataFile;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FileSystemRepository(@Value("${app.storage.path}") String path) {
        this.storagePath = Paths.get(path);
        this.metadataFile = storagePath.resolve("metadata.json");
        initializeStorage();
    }

    private void initializeStorage() {
        try {
            Files.createDirectories(storagePath);
            if (!Files.exists(metadataFile)) {
                Files.createFile(metadataFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("Storage initialization failed", e);
        }
    }

    @Override
    public FileModel findByFileNameOrImageName(String name) {
        try {
            return loadMetadata().stream()
                    .filter(f -> f.getFileName().equals(name) || f.getImageName().equals(name))
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            throw new RuntimeException("Metadata read failed", e);
        }
    }

    @Override
    public void insertAFile(File txtFile, File imageFile) {
        try {
            Path txtPath = storagePath.resolve(txtFile.getName());
            Path imgPath = storagePath.resolve(imageFile.getName());
            Files.copy(txtFile.toPath(), txtPath);
            Files.copy(imageFile.toPath(), imgPath);

            FileModel model = new FileModel();
            model.setCreationDate(Date.from(Instant.now()));
            model.setFileName(txtFile.getName());
            model.setFileData(Files.readAllBytes(txtPath));
            model.setImageName(imageFile.getName());
            model.setImageType(FilenameUtils.getExtension(imageFile.getName()));
            model.setImageData(Files.readAllBytes(imgPath));

            List<FileModel> metadata = loadMetadata();
            metadata.add(model);
            saveMetadata(metadata);
        } catch (IOException e) {
            throw new RuntimeException("File storage failed", e);
        }
    }

    @Override
    public List<FileModel> getAllFiles() {
        try {
            return loadMetadata();
        } catch (IOException e) {
            throw new RuntimeException("Metadata read failed", e);
        }
    }

    @Override
    public void deleteFileByName(String name) {
        try {
            List<FileModel> metadata = loadMetadata();
            Optional<FileModel> file = metadata.stream()
                    .filter(f -> f.getFileName().equals(name) || f.getImageName().equals(name))
                    .findFirst();

            if (file.isPresent()) {
                Files.deleteIfExists(storagePath.resolve(file.get().getFileName()));
                Files.deleteIfExists(storagePath.resolve(file.get().getImageName()));
                metadata.remove(file.get());
                saveMetadata(metadata);
            }
        } catch (IOException e) {
            throw new RuntimeException("Deletion failed", e);
        }
    }

    private List<FileModel> loadMetadata() throws IOException {
        if (Files.size(metadataFile) == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(
                metadataFile.toFile(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, FileModel.class)
        );
    }

    private void saveMetadata(List<FileModel> metadata) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(metadataFile.toFile(), metadata);
    }
}