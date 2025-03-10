package com.ivanledakovich.database.mongodb;

import com.ivanledakovich.database.FileRepository;
import com.ivanledakovich.exceptions.StorageException;
import com.ivanledakovich.models.FileModel;
import com.ivanledakovich.models.MongoFileModel;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "app.storage.type", havingValue = "mongodb")
public class MongoFileRepository implements FileRepository {

    private final MongoTemplate mongoTemplate;

    public MongoFileRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private MongoFileModel convertToMongoModel(FileModel model) {
        MongoFileModel mongoFile = new MongoFileModel();
        mongoFile.setId(model.getId().toString());
        mongoFile.setCreationDate(model.getCreationDate());
        mongoFile.setFileName(model.getFileName());
        mongoFile.setFileData(model.getFileData());
        mongoFile.setImageName(model.getImageName());
        mongoFile.setImageType(model.getImageType());
        mongoFile.setImageData(model.getImageData());
        return mongoFile;
    }

    private FileModel convertFromMongoModel(MongoFileModel mongoFile) {
        FileModel model = new FileModel();
        model.setId(UUID.fromString(mongoFile.getId()));
        model.setCreationDate(mongoFile.getCreationDate());
        model.setFileName(mongoFile.getFileName());
        model.setFileData(mongoFile.getFileData());
        model.setImageName(mongoFile.getImageName());
        model.setImageType(mongoFile.getImageType());
        model.setImageData(mongoFile.getImageData());
        return model;
    }

    @Override
    public FileModel findByFileNameOrImageName(String name) {
        Query query = new Query(
                new Criteria().orOperator(
                        Criteria.where("fileName").is(name),
                        Criteria.where("imageName").is(name)
                )
        );
        MongoFileModel result = mongoTemplate.findOne(query, MongoFileModel.class);
        return result != null ? convertFromMongoModel(result) : null;
    }

    @Override
    public void insertAFile(File txtFile, File imageFile) {
        try {
            FileModel model = new FileModel();
            model.setId(UUID.randomUUID());
            model.setCreationDate(new Date());
            model.setFileName(txtFile.getName());
            model.setFileData(Files.readAllBytes(txtFile.toPath()));
            model.setImageName(imageFile.getName());
            model.setImageType(FilenameUtils.getExtension(imageFile.getName()));
            model.setImageData(Files.readAllBytes(imageFile.toPath()));

            mongoTemplate.insert(convertToMongoModel(model));
        } catch (IOException e) {
            throw new StorageException("Failed to store file in MongoDB", e);
        }
    }

    @Override
    public List<FileModel> getAllFiles() {
        return mongoTemplate.findAll(MongoFileModel.class).stream()
                .map(this::convertFromMongoModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFileByName(String name) {
        Query query = new Query(
                new Criteria().orOperator(
                        Criteria.where("fileName").is(name),
                        Criteria.where("imageName").is(name)
                )
        );
        mongoTemplate.remove(query, MongoFileModel.class);
    }
}