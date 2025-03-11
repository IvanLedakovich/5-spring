package com.ivanledakovich.config;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Configuration
@Profile("test")
public class EmbeddedMongoConfig {

    private MongodExecutable mongodExecutable;

    @Bean
    public MongodConfig mongodConfig() throws IOException {
        return MongodConfig.builder()
                .version(Version.Main.PRODUCTION)
                .net(new Net("localhost", 27017, Network.localhostIsIPv6()))
                .build();
    }

    @Bean
    public MongodExecutable mongodExecutable(MongodConfig mongodConfig) throws IOException {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
        return mongodExecutable;
    }

    @PreDestroy
    public void cleanUp() {
        if (mongodExecutable != null) {
            mongodExecutable.stop();
        }
    }
}