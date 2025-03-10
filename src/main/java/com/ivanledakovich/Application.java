package com.ivanledakovich;

import com.ivanledakovich.logic.*;
import com.ivanledakovich.models.Parameters;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

@SpringBootApplication
@EntityScan("com.ivanledakovich.models")
@EnableJpaRepositories(basePackages = "com.ivanledakovich.database.jpa")
@EnableMongoRepositories(basePackages = "com.ivanledakovich.database.mongodb")
public class Application implements CommandLineRunner {
	private final FileService fileService;

	public Application(FileService fileService) {
		this.fileService = fileService;
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
			ctx.close();
		} else {
			SpringApplication.run(Application.class, args);
		}
	}

	@Override
	public void run(String... args) throws Exception {
		if (args.length == 0) return;

		Parameters params = ArgumentsParser.parseArguments(args);
		List<Future<File>> futures = fileService.process(
				params.getTextFilePaths(),
				params.getImageFileType(),
				params.getImageSaveLocation()
		);

		fileService.awaitCompletion(futures);
		fileService.shutdown();
	}
}