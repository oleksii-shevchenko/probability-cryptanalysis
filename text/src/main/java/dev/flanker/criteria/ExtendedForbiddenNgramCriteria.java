package dev.flanker.criteria;

import dev.flanker.util.FrequencyUtil;
import dev.flanker.util.OrderingUtil;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ExtendedForbiddenNgramCriteria implements TextCriteria {
    private final Set<String> forbiddenNgram;

    private final int ngram;
    private final int critical;

    private ExtendedForbiddenNgramCriteria(Set<String> forbiddenNgram, int ngram, int critical) {
        this.forbiddenNgram = forbiddenNgram;
        this.ngram = ngram;
        this.critical = critical;
    }

    @Override
    public boolean isRandom(String text) {
        Set<String> marked = new HashSet<>();
        for (int i = 0; i < text.length(); i += ngram) {
            String target = text.substring(i, i + ngram);
            if (forbiddenNgram.contains(target)) {
                marked.add(target);
            }

            if (marked.size() >= critical) {
                return false;
            }
        }
        return marked.size() < critical;
    }

    public static ExtendedForbiddenNgramCriteria getCriteria(String text, int ngram, int forbidden, int critical) {
        Set<String> forbiddenNgram = OrderingUtil.tail(FrequencyUtil.frequencies(text, ngram), forbidden);

        return new ExtendedForbiddenNgramCriteria(forbiddenNgram, ngram, critical);
    }
}
