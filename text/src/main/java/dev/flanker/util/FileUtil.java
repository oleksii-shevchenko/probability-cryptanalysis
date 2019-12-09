package dev.flanker.util;


import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public final class FileUtil {
    private static final Pattern CLEAN_PATTERN = Pattern.compile("[^[a-z]]");

    private static final String EMPTY_CHAR = "";

    private FileUtil() { }

    public static String read(String path) {
        try {
            return IOUtils.toString(new FileInputStream(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String clean(String file) {
        return file.toLowerCase().replaceAll(CLEAN_PATTERN.pattern(), EMPTY_CHAR);
    }

    public static String readAndClean(String path) {
        return clean(read(path));
    }

    public static void write(String content, String path) {
        try (Writer writer = new BufferedWriter(new FileWriter(path))) {
            IOUtils.write(content, writer);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
