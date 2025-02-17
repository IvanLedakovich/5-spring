package com.ivanledakovich.config;

import com.ivanledakovich.database.FileDatabaseFunctions;
import com.ivanledakovich.database.FileRepository;
import com.ivanledakovich.database.FileSystemRepository;
import com.ivanledakovich.models.DatabaseConnectionProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(DatabaseConnectionProperties.class)
public class AppConfig {

    @Bean
    @ConditionalOnProperty(name = "app.storage.type", havingValue = "database")
    public FileRepository databaseFileRepository(DatabaseConnectionProperties dbProps) {
        return new FileDatabaseFunctions(dbProps);
    }

    @Bean
    @ConditionalOnProperty(name = "app.storage.type", havingValue = "filesystem")
    public FileRepository filesystemFileRepository(@Value("${app.storage.path}") String path) {
        return new FileSystemRepository(path);
    }

    @Bean(name = "saveLocations")
    @ConfigurationProperties(prefix = "app.save-locations")
    public Map<Integer, String> saveLocations() {
        return new java.util.LinkedHashMap<>();
    }
}