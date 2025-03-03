package com.ivanledakovich.config;

import com.ivanledakovich.database.MongoFileRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Profile("mongodb")
public class MongoConfig {

    @Bean
    public MongoFileRepository mongoFileRepository(MongoTemplate mongoTemplate) {
        return new MongoFileRepository(mongoTemplate);
    }
}
