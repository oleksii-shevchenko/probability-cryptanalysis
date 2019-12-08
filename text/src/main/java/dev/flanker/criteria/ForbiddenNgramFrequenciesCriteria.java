package dev.flanker.criteria;

import dev.flanker.util.FrequencyUtil;
import dev.flanker.util.OrderingUtil;

import java.util.Map;

public class ForbiddenNgramFrequenciesCriteria implements TextCriteria {
    private final Map<String, Double> forbiddenFrequencies;
    private final int ngram;

    private ForbiddenNgramFrequenciesCriteria(Map<String, Double> forbiddenFrequencies, int ngram) {
        this.forbiddenFrequencies = forbiddenFrequencies;
        this.ngram = ngram;
    }

    @Override
    public boolean isRandom(String text) {
        return FrequencyUtil.frequencies(text, ngram, forbiddenFrequencies.keySet())
                .entrySet()
                .stream()
                .allMatch(entry -> entry.getValue() > forbiddenFrequencies.get(entry.getKey()));

    }

    public static ForbiddenNgramFrequenciesCriteria getCriteria(String text, int ngram, int forbidden) {
        Map<String, Double> forbiddenFrequencies = OrderingUtil.tailMap(FrequencyUtil.frequencies(text, ngram), forbidden);

        return new ForbiddenNgramFrequenciesCriteria(forbiddenFrequencies, ngram);
    }

    @Override
    public String toString() {
        return "ForbiddenNgramFrequenciesCriteria{" +
                "forbiddenFrequencies=" + forbiddenFrequencies +
                ", ngram=" + ngram +
                '}';
    }
}
