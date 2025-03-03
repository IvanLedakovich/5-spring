package com.ivanledakovich.config;

import com.ivanledakovich.database.FileDatabaseFunctions;
import com.ivanledakovich.database.FileRepository;
import com.ivanledakovich.database.FileSystemRepository;
import com.ivanledakovich.database.MongoFileRepository;
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
    public FileRepository databaseFileRepository(DatabaseConnectionProperties dbProps) {
        return new FileDatabaseFunctions(dbProps);
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