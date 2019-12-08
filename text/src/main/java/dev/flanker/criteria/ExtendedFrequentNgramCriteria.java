package dev.flanker.criteria;

import dev.flanker.util.FrequencyUtil;
import dev.flanker.util.OrderingUtil;

import java.util.HashSet;
import java.util.Set;

public class ExtendedFrequentNgramCriteria implements TextCriteria {
    private final Set<String> frequentNgram;

    private final int ngram;
    private final int critical;

    private ExtendedFrequentNgramCriteria(Set<String> frequentNgram, int ngram, int critical) {
        this.frequentNgram = frequentNgram;
        this.ngram = ngram;
        this.critical = critical;
    }

    @Override
    public boolean isRandom(String text) {
        Set<String> marked = new HashSet<>();
        for (int i = 0; i < text.length(); i += ngram) {
            String target = text.substring(i, i + ngram);
            if (frequentNgram.contains(target)) {
                marked.add(target);
            }
            if (frequentNgram.size() - marked.size() < critical) {
                return true;
            }
        }
        return frequentNgram.size() - marked.size() < critical;
    }

    public static ExtendedFrequentNgramCriteria getCriteria(String text, int ngram, int frequent, int critical) {
        Set<String> forbiddenNgram = OrderingUtil.head(FrequencyUtil.frequencies(text, ngram), frequent);

        return new ExtendedFrequentNgramCriteria(forbiddenNgram, ngram, critical);
    }
}
