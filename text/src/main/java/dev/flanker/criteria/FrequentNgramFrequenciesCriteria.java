package dev.flanker.criteria;

import java.util.Map;

import dev.flanker.domain.Alphabet;
import dev.flanker.util.TextUtil;

public class FrequentNgramFrequenciesCriteria implements TextCriteria {
    private final Map<String, Double> headFrequencies;
    private final int ngram;

    private FrequentNgramFrequenciesCriteria(Map<String, Double> forbiddenFrequencies, int ngram) {
        this.headFrequencies = forbiddenFrequencies;
        this.ngram = ngram;
    }

    @Override
    public boolean isRandom(String text) {
        return TextUtil.frequencies(text, ngram, headFrequencies.keySet())
                .entrySet()
                .stream()
                .allMatch(entry -> entry.getValue() < headFrequencies.get(entry.getKey()));

    }

    public static FrequentNgramFrequenciesCriteria getCriteria(String text, int ngram, int frequent, Alphabet alphabet) {
        Map<String, Double> forbiddenFrequencies = TextUtil.headMap(TextUtil.frequencies(text, ngram, alphabet), frequent);

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
