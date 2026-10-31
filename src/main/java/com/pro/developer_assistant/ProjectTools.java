package com.pro.developer_assistant;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.stream.Stream;
import java.util.List;

@Component
public class ProjectTools {
    private static final String PROJECT_ROOT = "src/main/java";

    @Tool(description = "Lists all the Java source files in the designated project")
    public List<String> listProjectFiles() {
        try (Stream<Path> paths = Files.walk(Paths.get(PROJECT_ROOT))) {
            return paths.filter(Files::isRegularFile)

                    .map(Path::toString)
                    .toList();
        } catch (IOException e) {

            return List.of("Error reading project files");
        }
    }

    @Tool(description = "Searches for the keyword across java files and returns matching Java File names")
    public List<String> searchCodebase(String keyword) {
        try (Stream<Path> paths = Files.walk(Paths.get(PROJECT_ROOT))) {
            return paths.filter(Files::isRegularFile)
                    .filter(path -> containsKeyword(path, keyword))
                    .map(Path::toString)
                    .toList();
        } catch (IOException e) {
            return List.of("Error searching codebase" + e.getMessage());
        }
        // Implementation to search the codebase under PROJECT_ROOT
    }

    private boolean containsKeyword(Path path, String keyword) {
        try {
            String content = Files.readString(path);
            return content.contains(keyword);
        } catch (IOException e) {
            return false;
        }
    }
}
