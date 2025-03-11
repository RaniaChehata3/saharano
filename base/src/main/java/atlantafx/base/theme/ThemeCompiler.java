/* SPDX-License-Identifier: MIT */

package atlantafx.base.theme;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.sun.javafx.css.StyleManager;

/**
 * Simple utility to compile CSS files to BSS format.
 * JavaFX uses BSS binary stylesheets for faster loading.
 */
public class ThemeCompiler {

    /**
     * The main class that accepts exactly one parameter, which is the path to
     * the source directory to be scanned for CSS files.
     *
     * <p>Usage:
     * <pre>{@code
     * java ThemeCompiler <path>
     * }</pre>
     *
     * @see #convertToBinary(Path)
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: ThemeCompiler <directory>");
            System.exit(1);
        }

        String directory = args[0];
        System.out.println("Compiling CSS files in directory: " + directory);
        
        try {
            List<File> cssFiles = Files.walk(Paths.get(directory))
                .filter(path -> path.toString().endsWith(".css"))
                .map(Path::toFile)
                .collect(Collectors.toList());
            
            for (File cssFile : cssFiles) {
                try {
                    String cssPath = cssFile.getPath().replace('\\', '/');
                    String bssPath = cssPath.replace(".css", ".bss");
                    
                    System.out.println("Compiling: " + cssPath + " to " + bssPath);
                    
                    // Use JavaFX StyleManager to convert CSS to BSS
                    StyleManager.getInstance().addUserAgentStylesheet(cssPath);
                    
                    System.out.println("Successfully compiled: " + cssPath);
                } catch (Exception e) {
                    System.err.println("Failed to compile: " + cssFile.getPath());
                    e.printStackTrace();
                }
            }
            
            System.out.println("Compilation complete.");
        } catch (IOException e) {
            System.err.println("Error processing directory: " + directory);
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Converts all CSS files in the specified directory to BSS.
     *
     * @param dir The source directory to scan for CSS files.
     * @throws IOException to punish you for using Java
     */
    public void convertToBinary(Path dir) throws IOException {
        if (dir == null || !Files.exists(dir) || !Files.isDirectory(dir)) {
            throw new IllegalArgumentException("Invalid directory: " + dir);
        }

        try (Stream<Path> stream = Files.list(dir)) {
            stream.filter(f -> f.toString().endsWith(".css"))
                .forEach(f -> {
                    try {
                        convertToBinary(f, f.resolveSibling(getFilename(f) + ".bss"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        }
    }

    /**
     * Converts the specified CSS file to BSS. If no output file is given,
     * then the input file name is used with an extension of 'bss'.
     *
     * @param in  The input file path.
     * @param out The output file path.
     * @throws IOException to punish you for using Java
     */
    public void convertToBinary(Path in, Path out) throws IOException {
        if (in == null || out == null) {
            throw new IllegalArgumentException("Both input and output files must be specified.");
        }

        if (in.equals(out)) {
            throw new IllegalArgumentException("Input file and output file cannot be the same.");
        }

        Stylesheet.convertToBinary(in.toFile(), out.toFile());
    }

    private String getFilename(Path f) {
        String name = f.getFileName().toString();
        return name.substring(0, name.lastIndexOf('.'));
    }
}
