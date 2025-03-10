package com.ivanledakovich.database;

import com.ivanledakovich.database.jpa.FileJpaRepository;
import com.ivanledakovich.models.FileModel;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * JPA implementation of file storage using Spring Data JPA.
 * Implements CRUD operations for files in a PostgreSQL database.
 *
 * @author Ivan Ledakovich
 */
@Repository
@Primary
@ConditionalOnProperty(name = "app.storage.type", havingValue = "postgres")
public class JpaFileRepositoryImpl implements FileRepository {

    private final FileJpaRepository fileJpaRepository;

    public JpaFileRepositoryImpl(FileJpaRepository fileJpaRepository) {
        this.fileJpaRepository = fileJpaRepository;
    }

    @Override
    public FileModel findByFileNameOrImageName(String name) {
        return fileJpaRepository.findByFileNameOrImageName(name);
    }

    @Override
    @Transactional
    public void insertAFile(File txtFile, File imageFile) {
        try {
            FileModel fileModel = new FileModel();
            fileModel.setFileName(txtFile.getName());
            fileModel.setFileData(Files.readAllBytes(txtFile.toPath()));
            fileModel.setImageName(imageFile.getName());
            fileModel.setImageType(FilenameUtils.getExtension(imageFile.getName()));
            fileModel.setImageData(Files.readAllBytes(imageFile.toPath()));
            fileJpaRepository.save(fileModel);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store files", e);
        }
    }

    @Override
    public List<FileModel> getAllFiles() {
        return fileJpaRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteFileByName(String name) {
        fileJpaRepository.deleteByFileNameOrImageName(name);
    }
}