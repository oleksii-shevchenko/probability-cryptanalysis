package dev.flanker.criteria;

import dev.flanker.util.FrequencyUtil;
import dev.flanker.util.OrderingUtil;

import java.util.Map;

public class ExtendedFrequentNgramFrequenciesCriteria implements TextCriteria {
    private final Map<String, Double> headFrequencies;

    private final int ngram;
    private final int critical;

    private ExtendedFrequentNgramFrequenciesCriteria(Map<String, Double> forbiddenFrequencies, int ngram, int critical) {
        this.headFrequencies = forbiddenFrequencies;
        this.ngram = ngram;
        this.critical = critical;
    }

    @Override
    public boolean isRandom(String text) {
        return critical > FrequencyUtil.frequencies(text, ngram, headFrequencies.keySet())
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() < headFrequencies.get(entry.getKey()))
                .count();

    }

    public static ExtendedFrequentNgramFrequenciesCriteria getCriteria(String text, int ngram, int frequent, int critical) {
        Map<String, Double> forbiddenFrequencies = OrderingUtil.headMap(FrequencyUtil.frequencies(text, ngram), frequent);

        return new ExtendedFrequentNgramFrequenciesCriteria(forbiddenFrequencies, ngram, critical);
    }
}
