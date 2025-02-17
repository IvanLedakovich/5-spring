package com.ivanledakovich;

import com.ivanledakovich.logic.ArgumentsParser;
import com.ivanledakovich.logic.FileProcessor;
import com.ivanledakovich.models.Parameters;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.WebApplicationType;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private final FileProcessor fileProcessor;

	public Application(FileProcessor fileProcessor) {
		this.fileProcessor = fileProcessor;
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class)
					.web(WebApplicationType.NONE)
					.run(args);
			context.close();
		} else {
			SpringApplication.run(Application.class, args);
		}
	}

	@Override
	public void run(String... args) throws Exception {
		if (args.length > 0) {
			Parameters params = ArgumentsParser.parseArguments(args);
			for (String path : params.getTextFilePaths()) {
				fileProcessor.configure(params.getImageFileType(),
						params.getImageSaveLocation(),
						path);
				fileProcessor.call();
			}
		}
	}
}