package dev.flanker.criteria;

import java.util.HashSet;
import java.util.Set;

import dev.flanker.domain.Alphabet;
import dev.flanker.util.TextUtil;

public class ExtendedFrequentNgramCriteria implements TextCriteria {
    private final Set<String> frequentNgram;
    private final int critical;
    private final int ngram;

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
            if (marked.size() >= critical) {
                return false;
            }
        }
        return marked.size() < critical;
    }

    public static ExtendedFrequentNgramCriteria getCriteria(String text, int ngram, int frequent, int critical, Alphabet alphabet) {
        Set<String> forbiddenNgram = TextUtil.head(TextUtil.frequencies(text, ngram, alphabet), frequent);

        return new ExtendedFrequentNgramCriteria(forbiddenNgram, ngram, critical);
    }

    @Override
    public String toString() {
        return "ExtendedFrequentNgramCriteria{" +
                "frequentNgram=" + frequentNgram +
                ", ngram=" + ngram +
                ", critical=" + critical +
                '}';
    }
}
