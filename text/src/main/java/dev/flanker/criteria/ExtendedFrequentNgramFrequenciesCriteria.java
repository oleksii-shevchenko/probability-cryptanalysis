package dev.flanker.criteria;

import java.util.Map;

import dev.flanker.domain.Alphabet;
import dev.flanker.util.TextUtil;

public class ExtendedFrequentNgramFrequenciesCriteria implements TextCriteria {
    private final Map<String, Double> headFrequencies;
    private final int critical;
    private final int ngram;

    private ExtendedFrequentNgramFrequenciesCriteria(Map<String, Double> forbiddenFrequencies, int ngram, int critical) {
        this.headFrequencies = forbiddenFrequencies;
        this.ngram = ngram;
        this.critical = critical;
    }

    @Override
    public boolean isRandom(String text) {
        return critical > TextUtil.frequencies(text, ngram, headFrequencies.keySet())
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() < headFrequencies.get(entry.getKey()))
                .count();

    }

    public static ExtendedFrequentNgramFrequenciesCriteria getCriteria(String text, int ngram, int frequent, int critical, Alphabet alphabet) {
        Map<String, Double> forbiddenFrequencies = TextUtil.headMap(TextUtil.frequencies(text, ngram, alphabet), frequent);

        return new ExtendedFrequentNgramFrequenciesCriteria(forbiddenFrequencies, ngram, critical);
    }

    @Override
    public String toString() {
        return "ExtendedFrequentNgramFrequenciesCriteria{" +
                "headFrequencies=" + headFrequencies +
                ", ngram=" + ngram +
                ", critical=" + critical +
                '}';
    }
}
