package Compiler.Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileDependencyResolver {

    public ArrayList<FileContent> resolveDependencies(String initialFilePath) throws IOException {
        ArrayList<FileContent> files = new ArrayList<>();
        resolveFile(initialFilePath, files);
        return files;
    }

    private void resolveFile(String filePath, ArrayList<FileContent> files) throws IOException {
        StringBuilder content = new StringBuilder();
        ArrayList<String> importPaths = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check if the line contains an import statement
                String importPath = parseImport(line, filePath);
                if (importPath != null) {
                    importPaths.add(importPath);
                } else {
                    content.append(line).append("\n");
                }
            }
        }

        // First, resolve dependencies of the current file
        for (String importPath : importPaths) {
            resolveFile(importPath, files);
        }

        // Then, add the current file content to the list
        files.add(new FileContent(filePath, content.toString()));
    }

    private String parseImport(String line, String initialPath) {
        // Simple regex for import statements, customize as needed
        Pattern pattern = Pattern.compile("^import\\s+\"(.+?)\"");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            Stack<String> pathStack = new Stack<String>();
            for(String path : initialPath.split("\\\\")){
                pathStack.push(path);
            }
            pathStack.pop(); // get rid of the file that contained the import

            String[] paths = matcher.group(1).split("\\\\");
            
            for(String path : paths){
                if(path == "."){
                    pathStack.pop();
                }
                else{
                    pathStack.push(path);
                }
            }

            return String.join("\\\\",pathStack);
        }
        return null;
    }

    // Nested class to represent file path and content
    public static class FileContent {
        private final String filePath;
        private final String content;

        public FileContent(String filePath, String content) {
            this.filePath = filePath;
            this.content = content;
        }

        public String getFilePath() {
            return filePath;
        }

        public String getContent() {
            return content;
        }
    }
}
