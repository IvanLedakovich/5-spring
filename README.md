# Java web application, converting text files into images and saving them into a database or file system with the functionality of downloading text files and images, made with Spring Boot and compatible with PostgreSQL / MongoDB

**The program accepts uploaded text type files from the Thymeleaf page, utilizes Java future, 
prints their contents in the terminal, saves them in a database or file system, depending on 
the configuration file, allows to view and download the uploaded text files and images, 
created from them. The images are generated according
to the type and output directory provided. The program utilizes the Gradle build tool and 
works in both CLI and web application modes.**

## How to run the program:
There are 2 ways to run the application: inside the IDE and as a .war or .jar standalone.

### Inside IDE (IntelliJ IDEA):
1. Provide the application with your own application.properties file in accordance to the 
example provided or with the environment variables through Run/Debug configuration to 
run in CLI mode.
2. Run through the IDE functionality or with the 
```
   ./gradlew bootRun
   ```
command in the terminal.

### As a .war or .jar standalone
1. Provide the application with your own application.properties file in accordance to the
example provided
2. Package with the 
```
   ./gradlew bootJar
   ```
or
```
   ./gradlew bootWar
   ```
terminal command
3. Run as a standard .jar or .war package and provide with command line arguments to run in 
CLI mode

What the command line arguments should look like to run in the CLI mode:
1. "--file-type" + Image type ("png", "jpg", etc.)
2. "--save-location" + Image save destination (e.g. "D:\Games")
3. "--file-path" + Initial .txt files separated by space (e.g. "D:\test.txt" "D:\test1.txt" etc.)

The program accepts arguments in any order. The full command for the .jar package to run in the CLI mode could 
look like this:

```
   java -jar .\5-spring-0.0.1-SNAPSHOT.jar --args='--file-type png --save-location D:\Games --file-path D:\test.txt D:\test1.txt D:\test2.txt'
   ```
## Two storage modes:
By default, the application works in the filesystem mode when it comes to storing the files. 
However, the application.properties file can be changed to utilize either a PostgreSQL or 
MongoDB databases. For that, change the app.storage.type variable and provide your credentials.

