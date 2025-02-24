package com.ivanledakovich.logic;

/**
 * This class contains the method which prints out the user manual in the terminal
 *
 * @author Ivan Ledakovich
 */
public class Help {

    /**
     * This method prints out user's manual in terminal
     */
    public static void help() {
        System.out.println(" The program must be provided with 3 arguments in any order:\n" +
                "	1. \"--file-type\" + Image type (\"png\", \"jpg\", etc.)\n" +
                "	2. \"--save-location\" + Image save destination (e.g. \"D:\\Games\")\n" +
                "	3. \"--file-path\" + Initial .txt files, separated by space (e.g. \"D:\\test.txt\" \"D:\\test1.txt\" etc.)\n" +
                "	The full command could look like this:\n" +
                "	\n" +
                "	java -jar .\\5-spring-0.0.1-SNAPSHOT.jar \"--file-type\" \"png\" \"--save-location\" \"D:\\Games\" \"--file-path\" \"D:\\test.txt\" \"D:\\test1.txt\" \"D:\\test2.txt\"");
    }
}
