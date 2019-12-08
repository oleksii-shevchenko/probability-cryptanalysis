package dev.flanker.criteria;

import dev.flanker.util.FrequencyUtil;
import dev.flanker.util.OrderingUtil;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ForbiddenNgramCriteria implements TextCriteria {
    private final Set<String> forbiddenNgram;
    private final int ngram;

    private ForbiddenNgramCriteria(Set<String> forbiddenNgram, int ngram) {
        this.forbiddenNgram = forbiddenNgram;
        this.ngram = ngram;
    }

    @Override
    public boolean isRandom(String text) {
        for (int i = 0; i < text.length(); i += ngram) {
            if (forbiddenNgram.contains(text.substring(i, i + ngram))) {
                return true;
            }
        }
        return false;
    }

    public static ForbiddenNgramCriteria getCriteria(String text, int ngram, int forbidden) {
        Set<String> forbiddenNgram = OrderingUtil.tail(FrequencyUtil.frequencies(text, ngram), forbidden);

        return new ForbiddenNgramCriteria(forbiddenNgram, ngram);
    }

    @Override
    public String toString() {
        return "ForbiddenNgramCriteria{" +
                "forbiddenNgram=" + forbiddenNgram +
                ", ngram=" + ngram +
                '}';
    }
}
