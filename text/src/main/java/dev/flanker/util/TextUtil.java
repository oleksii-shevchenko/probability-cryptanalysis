package dev.flanker.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import dev.flanker.domain.Alphabet;

public final class TextUtil {
    private TextUtil() { }

    public static Map<String, Double> frequencies(String text, int ngram, Alphabet alphabet) {
        Map<String, Long> countsMap = init(alphabet, ngram);
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

    public static double entropy(String text, int ngram, Alphabet alphabet) {
        double entr = 0.0;
        for (Double frequency : frequencies(text, ngram, alphabet).values()) {
            if (frequency != 0.0) {
                entr += (frequency * ComputeUtil.log(frequency));
            }
        }
        return -entr / ngram;
    }

    public static double correspondenceIndex(String text, int ngram, Alphabet alphabet) {
        Collection<Double> frequencies = frequencies(text, ngram, alphabet).values();

        double index = 0.0;
        for (Double frequency : frequencies) {
            index += (frequency * (frequency - 1.0));
        }

        return index / (text.length() * (text.length() - 1));
    }

    public static Map<String, Double> tailMap(Map<String, Double> map, int size) {
        return map.entrySet()
                .stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .limit(size)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Set<String> tail(Map<String, Double> map, int size) {
        return map.entrySet()
                .stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .limit(size)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public static Map<String, Double> headMap(Map<String, Double> map, int size) {
        Comparator<Map.Entry<String, Double>> comparator = Comparator.comparingDouble(Map.Entry::getValue);
        return map.entrySet()
                .stream()
                .sorted(comparator.reversed())
                .limit(size)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Set<String> head(Map<String, Double> map, int size) {
        Comparator<Map.Entry<String, Double>> comparator = Comparator.comparingDouble(Map.Entry::getValue);
        return map.entrySet()
                .stream()
                .sorted(comparator.reversed())
                .limit(size)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    private static Map<String, Double> getStringDoubleMap(double textLength, double ngram, Map<String, Long> countsMap) {
        Map<String, Double> frequenciesMap = new HashMap<>();
        double ngramNumber = textLength / ngram;
        for (Map.Entry<String, Long> entry : countsMap.entrySet()) {
            frequenciesMap.put(entry.getKey(), entry.getValue() / ngramNumber);
        }
        return frequenciesMap;
    }

    private static Map<String, Long> init(Alphabet alphabet, int ngram) {
        Map<String, Long> map = new HashMap<>();
        if (ngram == 1) {
            for (int i = 0; i < alphabet.size(); i++) {
                map.put(String.valueOf(alphabet.symbol(i)), 0L);
            }
        }
        if (ngram == 2) {
            for (int i = 0; i < alphabet.size(); i++) {
                for (int j = 0; j < alphabet.size(); j++) {
                    map.put(ComputeUtil.compose(alphabet.symbol(i), alphabet.symbol(j)), 0L);
                }
            }
        }
        return map;
    }
}
