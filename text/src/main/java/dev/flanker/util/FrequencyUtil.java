package dev.flanker.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class FrequencyUtil {
    private FrequencyUtil() { }

    public static Map<String, Double> frequencies(String text, int ngram) {
        Map<String, Long> countsMap = new HashMap<>();

        for (int i = 0; i < text.length(); i += ngram) {
            String substr = text.substring(i, i + ngram);
            countsMap.put(substr, countsMap.getOrDefault(substr, 0L) + 1L);
        }

        return getStringDoubleMap(text, ngram, countsMap);
    }

    public static Map<String, Double> frequencies(String text, int ngram, Set<String> ngramSet) {
        Map<String, Long> countsMap = new HashMap<>();

        for (int i = 0; i < text.length(); i += ngram) {
            String substr = text.substring(i, i + ngram);
            if (ngramSet.contains(substr)) {
                countsMap.put(substr, countsMap.getOrDefault(substr, 0L) + 1L);
            }
        }

        return getStringDoubleMap(text, ngram, countsMap);
    }

    private static Map<String, Double> getStringDoubleMap(String text, int ngram, Map<String, Long> countsMap) {
        Map<String, Double> frequenciesMap = new HashMap<>();

        double ngramNumber = text.length() / ngram;
        for (Map.Entry<String, Long> entry : countsMap.entrySet()) {
            frequenciesMap.put(entry.getKey(), entry.getValue() / ngramNumber);
        }
        return frequenciesMap;
    }
}
