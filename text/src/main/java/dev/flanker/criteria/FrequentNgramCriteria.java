package dev.flanker.criteria;

import dev.flanker.util.FrequencyUtil;
import dev.flanker.util.OrderingUtil;

import java.util.HashSet;
import java.util.Set;

public class FrequentNgramCriteria implements TextCriteria {
    private final Set<String> frequentNgram;
    private final int ngram;

    private FrequentNgramCriteria(Set<String> frequentNgram, int ngram) {
        this.frequentNgram = frequentNgram;
        this.ngram = ngram;
    }

    @Override
    public boolean isRandom(String text) {
        Set<String> marked = new HashSet<>();
        for (int i = 0; i < text.length(); i += ngram) {
            String target = text.substring(i, i + ngram);
            if (frequentNgram.contains(target)) {
                marked.add(target);
            }
            if (marked.size() == frequentNgram.size()) {
                return true;
            }
        }
        return marked.size() == frequentNgram.size();
    }

    public static FrequentNgramCriteria getCriteria(String text, int ngram, int frequent) {
        Set<String> forbiddenNgram = OrderingUtil.head(FrequencyUtil.frequencies(text, ngram), frequent);

        return new FrequentNgramCriteria(forbiddenNgram, ngram);
    }
}
