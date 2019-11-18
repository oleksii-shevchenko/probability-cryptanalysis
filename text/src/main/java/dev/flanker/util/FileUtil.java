package dev.flanker.util;


import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
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
}
