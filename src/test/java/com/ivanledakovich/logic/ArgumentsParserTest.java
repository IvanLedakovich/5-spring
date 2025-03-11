package com.ivanledakovich.logic;

import com.ivanledakovich.models.Parameters;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentsParserTest {

    @Test
    void parseArgumentsWithValidArgsReturnsCorrectParameters() {
        String[] args = {
                "--file-type", "png",
                "--save-location", "/save/path",
                "--file-path", "file1.txt", "file2.txt"
        };

        Parameters params = ArgumentsParser.parseArguments(args);

        assertEquals("png", params.getImageFileType());
        assertEquals("/save/path", params.getImageSaveLocation());
        assertEquals(Arrays.asList("file1.txt", "file2.txt"), params.getTextFilePaths());
    }

    @Test
    void parseArgumentsWithMissingArgsShowsHelp() {
        String[] args = {"--help"};
        assertDoesNotThrow(() -> ArgumentsParser.parseArguments(args));
    }
}