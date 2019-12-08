package dev.flanker;

import dev.flanker.util.FileUtil;
import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SimpleRunner {
    public static void main(String[] args) throws IOException {
        String s = FileUtil.readAndClean("data\\raw.txt");

        try (Writer writer = new BufferedWriter(new FileWriter("data\\clean.txt"))) {
            IOUtils.write(s, writer);
        }
    }
}
