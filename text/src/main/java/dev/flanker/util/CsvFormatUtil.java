package dev.flanker.util;

import dev.flanker.criteria.EntropyCriteria;
import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Comparator;
import java.util.Map;

public final class CsvFormatUtil {
    private static final int A_SHIFT = 97;
    private static final int ENGLISH_ALPHABET_SIZE = 26;

    private CsvFormatUtil() { }

    public static void writeCsv(String csv, String path) {
        try (Writer writer = new BufferedWriter(new FileWriter(path))) {
            IOUtils.write(csv, writer);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String formatFrequencies(Map<String, Double> frequencies) {
        Comparator<Map.Entry<String, Double>> comparator = Comparator.comparingDouble(Map.Entry::getValue);
        return frequencies.entrySet()
                .stream()
                .sorted(comparator.reversed())
                .map(entry -> entry.getKey() + "," + entry.getValue() + "\n")
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public static String formatBigramFrequencies(Map<String, Double> frequencies) {
        StringBuilder csv = new StringBuilder(",");
        for (int i = A_SHIFT; i < A_SHIFT + ENGLISH_ALPHABET_SIZE; i++) {
            csv.append((char) i);
            if (i != A_SHIFT + ENGLISH_ALPHABET_SIZE - 1) {
                csv.append(",");
            } else {
                csv.append("\n");
            }
        }
        for (int i = 0; i < ENGLISH_ALPHABET_SIZE; i++) {
            csv.append((char) (i + A_SHIFT)).append(",");
            for (int j = 0; j < ENGLISH_ALPHABET_SIZE; j++) {
                Double frequency = frequencies.get(compose(i, j));
                if (frequency != null) {
                    csv.append(frequency);
                } else {
                    csv.append("0.0");
                }
                if (j != ENGLISH_ALPHABET_SIZE - 1) {
                    csv.append(",");
                } else {
                    csv.append("\n");
                }
            }
        }
        return csv.toString();
    }

    private static String compose(int a, int b) {
        char x = (char) (a + A_SHIFT);
        char y = (char) (b + A_SHIFT);
        return "" + x + y;
    }
}
