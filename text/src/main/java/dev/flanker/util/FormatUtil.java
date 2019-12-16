package dev.flanker.util;

import java.util.Comparator;
import java.util.Map;

import dev.flanker.criteria.TextCriteria;
import dev.flanker.domain.Alphabet;

public final class FormatUtil {

    private FormatUtil() { }

    public static String formatFrequencies(Map<String, Double> frequencies) {
        Comparator<Map.Entry<String, Double>> comparator = Comparator.comparingDouble(Map.Entry::getValue);
        return frequencies.entrySet()
                .stream()
                .sorted(comparator.reversed())
                .map(entry -> entry.getKey() + "," + entry.getValue() + "\n")
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public static String formatBigramFrequencies(Map<String, Double> frequencies, Alphabet alphabet) {
        StringBuilder csv = new StringBuilder(",");
        for (Character letter : alphabet.letters()) {
            csv.append(letter).append(",");
        }
        csv.replace(csv.length() - 1, csv.length(), "\n");

        for (int i = 0; i < alphabet.size(); i++) {
            csv.append(alphabet.symbol(i)).append(",");
            for (int j = 0; j < alphabet.size(); j++) {
                Double frequency = frequencies.get(ComputeUtil.compose(alphabet.symbol(i), alphabet.symbol(j)));
                if (frequency != null) {
                    csv.append(frequency);
                } else {
                    csv.append("0.0");
                }
                if (j != alphabet.size() - 1) {
                    csv.append(",");
                } else {
                    csv.append("\n");
                }
            }
        }
        return csv.toString();
    }

    public static String formatCriteriaConfig(Map<String, TextCriteria> criteria) {
        StringBuilder txt = new StringBuilder();
        for (TextCriteria value : criteria.values()) {
            txt.append(value.toString()).append("\n");
        }
        return txt.toString();
    }
}
