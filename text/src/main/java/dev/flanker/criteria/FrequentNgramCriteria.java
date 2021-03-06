package dev.flanker.criteria;

import java.util.HashSet;
import java.util.Set;

import dev.flanker.domain.Alphabet;
import dev.flanker.util.TextUtil;

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
                return false;
            }
        }
        return marked.size() != frequentNgram.size();
    }

    public static FrequentNgramCriteria getCriteria(String text, int ngram, int frequent, Alphabet alphabet) {
        Set<String> forbiddenNgram = TextUtil.head(TextUtil.frequencies(text, ngram, alphabet), frequent);
        return new FrequentNgramCriteria(forbiddenNgram, ngram);
    }

    @Override
    public String toString() {
        return "FrequentNgramCriteria{" +
                "frequentNgram=" + frequentNgram +
                ", ngram=" + ngram +
                '}';
    }
}
