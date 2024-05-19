package Compiler.Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import Compiler.Location;

public class ErrorFormatter {

    
    public static String format(String errorMessage, Location location) {
        StringBuilder builder = new StringBuilder();
        builder.append("In file: ");
        builder.append(location.getFilePath());
        builder.append("\n");
        builder.append(errorMessage);
        builder.append("\n");
    
        // Read the file to get the specific line and column
        try (BufferedReader reader = new BufferedReader(new FileReader(location.getFilePath()))) {
            String line;
            int currentLine = -1;
            while ((line = reader.readLine()) != null) {
                if (currentLine == location.getLine()) {
                    builder.append("Error at line ")
                           .append(location.getLine())
                           .append(", column ")
                           .append(location.getCol() - 1)
                           .append(":\n");
    
                    builder.append(line).append("\n");
    
                    // Highlight the specific column (optional)
                    for (int i = 0; i < location.getCol() - 1; i++) {
                        builder.append(" ");
                    }
                    builder.append("^\n");
    
                    break;
                }
                currentLine++;
            }
        } catch (IOException e) {
            builder.append("Error reading file: ").append(e.getMessage()).append("\n");
        }
    
        return builder.toString();
    }
    
}
