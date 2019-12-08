package dev.flanker.util;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class OrderingUtil {
    private OrderingUtil() { }

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
}
