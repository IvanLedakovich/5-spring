package com.ivanledakovich;

import com.ivanledakovich.logic.*;
import com.ivanledakovich.models.Parameters;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

@SpringBootApplication
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
		List<Future<File>> futures = fileService.processFilesInCLIMode(
				params.getTextFilePaths(),
				params.getImageFileType(),
				params.getImageSaveLocation()
		);

		fileService.awaitCompletion(futures);
		fileService.shutdown();
	}
}