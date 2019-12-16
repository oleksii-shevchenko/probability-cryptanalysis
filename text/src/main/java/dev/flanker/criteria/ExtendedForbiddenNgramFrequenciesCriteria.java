package dev.flanker.criteria;

import java.util.Map;

import dev.flanker.domain.Alphabet;
import dev.flanker.util.TextUtil;

public class ExtendedForbiddenNgramFrequenciesCriteria implements TextCriteria {
    private final Map<String, Double> forbiddenFrequencies;
    private final int critical;
    private final int ngram;

    private ExtendedForbiddenNgramFrequenciesCriteria(Map<String, Double> forbiddenFrequencies, int ngram, int critical) {
        this.forbiddenFrequencies = forbiddenFrequencies;
        this.ngram = ngram;
        this.critical = critical;
    }

    @Override
    public boolean isRandom(String text) {
        return critical < TextUtil.frequencies(text, ngram, forbiddenFrequencies.keySet())
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > forbiddenFrequencies.get(entry.getKey()))
                .count();

    }

    public static ExtendedForbiddenNgramFrequenciesCriteria getCriteria(String text, int ngram, int forbidden, int critical, Alphabet alphabet) {
        Map<String, Double> forbiddenFrequencies = TextUtil.tailMap(TextUtil.frequencies(text, ngram, alphabet), forbidden);

        return new ExtendedForbiddenNgramFrequenciesCriteria(forbiddenFrequencies, ngram, critical);
    }

    @Override
    public String toString() {
        return "ExtendedForbiddenNgramFrequenciesCriteria{" +
                "forbiddenFrequencies=" + forbiddenFrequencies +
                ", ngram=" + ngram +
                ", critical=" + critical +
                '}';
    }
}
