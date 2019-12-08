package dev.flanker.criteria;

import dev.flanker.util.FrequencyUtil;
import dev.flanker.util.OrderingUtil;

import java.util.Map;

public class FrequentNgramFrequenciesCriteria implements TextCriteria {
    private final Map<String, Double> headFrequencies;
    private final int ngram;

    private FrequentNgramFrequenciesCriteria(Map<String, Double> forbiddenFrequencies, int ngram) {
        this.headFrequencies = forbiddenFrequencies;
        this.ngram = ngram;
    }

    @Override
    public boolean isRandom(String text) {
        return FrequencyUtil.frequencies(text, ngram, headFrequencies.keySet())
                .entrySet()
                .stream()
                .noneMatch(entry -> entry.getValue() < headFrequencies.get(entry.getKey()));

    }

    public static FrequentNgramFrequenciesCriteria getCriteria(String text, int ngram, int frequent) {
        Map<String, Double> forbiddenFrequencies = OrderingUtil.headMap(FrequencyUtil.frequencies(text, ngram), frequent);

        return new FrequentNgramFrequenciesCriteria(forbiddenFrequencies, ngram);
    }

    @Override
    public String toString() {
        return "FrequentNgramFrequenciesCriteria{" +
                "headFrequencies=" + headFrequencies +
                ", ngram=" + ngram +
                '}';
    }
}
