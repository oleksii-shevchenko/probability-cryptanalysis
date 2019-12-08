package dev.flanker.util;

import java.util.*;
import java.util.stream.Collectors;

public final class FrequencyUtil {
    private static final int ENGLISH_ALPHABET_SIZE = 26;

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

    public static double entropy(String text, int ngram) {
        Collection<Double> frequencies = frequencies(text, ngram).values();

        double entr = 0.0;
        for (Double frequency : frequencies) {
            if (frequency != 0.0) {
                entr += (frequency * Math.log(frequency));
            }
        }

        return -entr / ngram;
    }

    public static double correspondenceIndex(String text, int ngram) {
        Collection<Double> frequencies = frequencies(text, ngram).values();

        double index = 0.0;
        for (Double frequency : frequencies) {
            index += (frequency * (frequency - 1.0));
        }

        return index / (text.length() * (text.length() - 1));
    }

    private static Map<String, Double> getStringDoubleMap(String text, double ngram, Map<String, Long> countsMap) {
        Map<String, Double> frequenciesMap = new HashMap<>();

        double ngramNumber = text.length() / ngram;
        for (Map.Entry<String, Long> entry : countsMap.entrySet()) {
            frequenciesMap.put(entry.getKey(), entry.getValue() / ngramNumber);
        }
        return frequenciesMap;
    }
}
