package dev.flanker.util;

import static dev.flanker.util.ComputeUtil.A_SHIFT;
import static dev.flanker.util.ComputeUtil.ENGLISH_ALPHABET_SIZE;

import java.util.Comparator;
import java.util.Map;

public final class CsvFormatUtil {

    private CsvFormatUtil() { }

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
                Double frequency = frequencies.get(ComputeUtil.compose(i, j));
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
}
