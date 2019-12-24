package dev.flanker.util;


import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

public final class FileUtil {
    private static final Pattern CLEAN_PATTERN = Pattern.compile("[^[a-z]]");
    private static final Pattern CYRILLIC_CLEAN_PATTERN = Pattern.compile("[^[а-яё]]");

    private static final String EMPTY_CHAR = "";

    private FileUtil() { }

    public static String read(Path path) {
        try {
            return IOUtils.toString(new FileInputStream(path.toString()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String clean(String content) {
        return CLEAN_PATTERN.matcher(content.toLowerCase()).replaceAll(EMPTY_CHAR);
    }

    public static String cleanCyrillic(String content) {
        return CYRILLIC_CLEAN_PATTERN.matcher(content.toLowerCase())
                .replaceAll(EMPTY_CHAR)
                .replaceAll("ё", "е")
                .replace("ъ", "ь");
    }

    public static void write(String content, Path path) {
        try (Writer writer = new BufferedWriter(new FileWriter(path.toString()))) {
            IOUtils.write(content, writer);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String sample(String origin, int length) {
        int offset = ThreadLocalRandom.current().nextInt(origin.length() - length);
        return origin.substring(offset, offset + length);
    }
}
