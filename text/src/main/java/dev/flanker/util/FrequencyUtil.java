package dev.flanker.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class FrequencyUtil {
    private FrequencyUtil() { }

    public static Map<String, Double> frequencies(String text, int ngram) {
        Map<String, Long> countsMap = init(ngram);
        for (int i = 0; i < text.length() - ngram + 1; i += ngram) {
            String substr = text.substring(i, i + ngram);
            countsMap.put(substr, countsMap.getOrDefault(substr, 0L) + 1L);
        }
        return getStringDoubleMap(text.length(), ngram, countsMap);
    }

    public static Map<String, Double> frequencies(String text, int ngram, Set<String> ngramSet) {
        Map<String, Long> countsMap = new HashMap<>();
        for (String target : ngramSet) {
            countsMap.put(target, 0L);
        }
        for (int i = 0; i < text.length() - ngram + 1; i += ngram) {
            String substr = text.substring(i, i + ngram);
            if (ngramSet.contains(substr)) {
                countsMap.put(substr, countsMap.getOrDefault(substr, 0L) + 1L);
            }
        }
        return getStringDoubleMap(text.length(), ngram, countsMap);
    }

    public static double entropy(String text, int ngram) {
        double entr = 0.0;
        for (Double frequency : frequencies(text, ngram).values()) {
            if (frequency != 0.0) {
                entr += (frequency * ComputeUtil.log(frequency));
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

    private static Map<String, Double> getStringDoubleMap(double textLength, double ngram, Map<String, Long> countsMap) {
        Map<String, Double> frequenciesMap = new HashMap<>();
        double ngramNumber = textLength / ngram;
        for (Map.Entry<String, Long> entry : countsMap.entrySet()) {
            frequenciesMap.put(entry.getKey(), entry.getValue() / ngramNumber);
        }
        return frequenciesMap;
    }

    private static Map<String, Long> init(int ngram) {
        Map<String, Long> map = new HashMap<>();

        if (ngram == 1) {
            for (int i = 0; i < ComputeUtil.ENGLISH_ALPHABET_SIZE; i++) {
                map.put(ComputeUtil.denormalizeString(i), 0L);
            }
        }

        if (ngram == 2) {
            for (int i = 0; i < ComputeUtil.ENGLISH_ALPHABET_SIZE; i++) {
                for (int j = 0; j < ComputeUtil.ENGLISH_ALPHABET_SIZE; j++) {
                    map.put(ComputeUtil.compose(i, j), 0L);
                }
            }
        }

        return map;
    }
}
