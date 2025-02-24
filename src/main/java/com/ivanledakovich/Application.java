package com.ivanledakovich;

import com.ivanledakovich.logic.ArgumentsParser;
import com.ivanledakovich.logic.FileReader;
import com.ivanledakovich.logic.ImageCreator;
import com.ivanledakovich.models.Parameters;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		if (args.length > 0) {
			Parameters params = ArgumentsParser.parseArguments(args);
			for (String path : params.getTextFilePaths()) {
				try {
					processFileDirectly(
							path,
							params.getImageFileType(),
							params.getImageSaveLocation()
					);
				} catch (IOException e) {
					System.err.println("Failed to process file: " + path);
					e.printStackTrace();
				}
			}
		} else {
			SpringApplication.run(Application.class, args);
		}
	}

	@Override
	public void run(String... args) {
	}

	private static void processFileDirectly(
			String textFilePath,
			String imageType,
			String saveLocation
	) throws IOException {
		File originalTxtFile = new File(textFilePath);

		String data = FileReader.readFile(originalTxtFile.getAbsolutePath());

		BufferedImage image = ImageCreator.createImage(data);

		File saveDir = new File(saveLocation);
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}

		String imageName = originalTxtFile.getName() + "." + imageType;
		File imageFile = new File(saveDir, imageName);
		ImageIO.write(image, imageType, imageFile);

		System.out.println("Saved image to: " + imageFile.getAbsolutePath());
	}
}