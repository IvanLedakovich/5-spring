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
1. Adjust the application.properties file to your requirements if needed or provide the application 
2. with the environment variables through Run/Debug configuration to 
run in CLI mode.
2. Run through the IDE functionality or with the 
```
   ./gradlew bootRun
   ```
command in the terminal.

### As a .war or .jar standalone
1. Adjust the application.properties file to your requirements if needed
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
The application.properties file must be present in src/main/resources.

Here is the default application.properties file:
```
spring.application.name=5-spring

spring.datasource.url=jdbc:postgresql://localhost:5432/4-database
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

spring.sql.init.mode=always

spring.jpa.hibernate.ddl-auto=update
spring.jpa.generate-ddl=true
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration

spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB

app.storage.type=filesystem
app.storage.path=./storage

app.save-locations.0=./storage/folder1
app.save-locations.1=./storage/folder2

   ```

