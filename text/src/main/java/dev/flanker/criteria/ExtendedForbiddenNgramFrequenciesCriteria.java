package dev.flanker.criteria;

import dev.flanker.util.FrequencyUtil;
import dev.flanker.util.OrderingUtil;

import java.util.Map;

public class ExtendedForbiddenNgramFrequenciesCriteria implements TextCriteria {
    private final Map<String, Double> forbiddenFrequencies;

    private final int ngram;
    private final int critical;

    private ExtendedForbiddenNgramFrequenciesCriteria(Map<String, Double> forbiddenFrequencies, int ngram, int critical) {
        this.forbiddenFrequencies = forbiddenFrequencies;
        this.ngram = ngram;
        this.critical = critical;
    }

    @Override
    public boolean isRandom(String text) {
        return critical > FrequencyUtil.frequencies(text, ngram, forbiddenFrequencies.keySet())
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > forbiddenFrequencies.get(entry.getKey()))
                .count();

    }

    public static ExtendedForbiddenNgramFrequenciesCriteria getCriteria(String text, int ngram, int forbidden, int critical) {
        Map<String, Double> forbiddenFrequencies = OrderingUtil.tailMap(FrequencyUtil.frequencies(text, ngram), forbidden);

        return new ExtendedForbiddenNgramFrequenciesCriteria(forbiddenFrequencies, ngram, critical);
    }
}
