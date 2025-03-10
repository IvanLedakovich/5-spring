package com.ivanledakovich.config;

import com.ivanledakovich.database.*;
import com.ivanledakovich.database.jpa.FileJpaRepository;
import com.ivanledakovich.database.mongodb.MongoFileRepository;
import com.ivanledakovich.models.DatabaseConnectionProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(DatabaseConnectionProperties.class)
public class AppConfig {

    @Bean
    @ConditionalOnProperty(name = "app.storage.type", havingValue = "postgres")
    public FileRepository databaseFileRepository(FileJpaRepository fileJpaRepository) {
        return new JpaFileRepositoryImpl(fileJpaRepository);
    }

    @Bean
    @ConditionalOnProperty(name = "app.storage.type", havingValue = "filesystem", matchIfMissing = true)
    public FileRepository filesystemFileRepository(
            @Value("${app.storage.path:./storage}") String path) {
        return new FileSystemRepository(path);
    }

    @Bean
    @ConditionalOnProperty(name = "app.storage.type", havingValue = "mongodb")
    public FileRepository mongoFileRepository(MongoTemplate mongoTemplate) {
        return new MongoFileRepository(mongoTemplate);
    }

    @Bean(name = "saveLocations")
    @ConfigurationProperties(prefix = "app.save-locations")
    public Map<Integer, String> saveLocations() {
        return new LinkedHashMap<>();
    }
}