package dev.flanker.util;

import java.util.*;
import java.util.Map.Entry;

public final class OrderingUtil {
    private OrderingUtil() { }

    public static Map<String, Double> tail(Map<String, Double> map, Comparator<Entry<String, Double>> comparator, int size) {
        NavigableSet<Entry<String, Double>> navigableSet = new TreeSet<>(comparator);
        navigableSet.addAll(map.entrySet());

        Map<String, Double> tailMap = new HashMap<>();
        for (Entry<String, Double> entry : navigableSet) {
            if (size > 0) {
                tailMap.put(entry.getKey(), entry.getValue());
                size--;
            }
        }

        return Collections.unmodifiableMap(tailMap);
    }
}
