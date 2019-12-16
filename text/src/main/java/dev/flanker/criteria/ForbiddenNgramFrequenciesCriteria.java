package dev.flanker.criteria;

import java.util.Map;

import dev.flanker.domain.Alphabet;
import dev.flanker.util.TextUtil;

public class ForbiddenNgramFrequenciesCriteria implements TextCriteria {
    private final Map<String, Double> forbiddenFrequencies;
    private final int ngram;

    private ForbiddenNgramFrequenciesCriteria(Map<String, Double> forbiddenFrequencies, int ngram) {
        this.forbiddenFrequencies = forbiddenFrequencies;
        this.ngram = ngram;
    }

    @Override
    public boolean isRandom(String text) {
        return TextUtil.frequencies(text, ngram, forbiddenFrequencies.keySet())
                .entrySet()
                .stream()
                .allMatch(entry -> entry.getValue() > forbiddenFrequencies.get(entry.getKey()));

    }

    public static ForbiddenNgramFrequenciesCriteria getCriteria(String text, int ngram, int forbidden, Alphabet alphabet) {
        Map<String, Double> forbiddenFrequencies = TextUtil.tailMap(TextUtil.frequencies(text, ngram, alphabet), forbidden);
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
