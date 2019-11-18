package dev.flanker.criteria;

import dev.flanker.util.FrequencyUtil;
import dev.flanker.util.OrderingUtil;

import java.util.*;

@SuppressWarnings("FieldCanBeLocal")
public class ForbiddenNgramFrequenciesCriteria implements TextCriteria {
    private static int DEFAULT_FORBIDDEN_SIZE = 20;

    private static int DEFAULT_CRITICAL_VALUE = 10;

    private final Map<String, Double> forbiddenFrequencies;

    private final int ngram;

    private ForbiddenNgramFrequenciesCriteria(Map<String, Double> forbiddenFrequencies, int ngram) {
        this.forbiddenFrequencies = forbiddenFrequencies;
        this.ngram = ngram;
    }

    public Map<String, Double> getForbiddenFrequencies() {
        return forbiddenFrequencies;
    }

    public int getNgram() {
        return ngram;
    }

    @Override
    public boolean isRandom(String text) {
        Map<String, Double> textFrequencies = FrequencyUtil.frequencies(text, ngram, forbiddenFrequencies.keySet());

        int criticalCount = 0;
        for (Map.Entry<String, Double> entry : textFrequencies.entrySet()) {
            if (entry.getValue() > forbiddenFrequencies.get(entry.getKey())) {
                criticalCount++;
            }
            if (criticalCount >= DEFAULT_CRITICAL_VALUE) {
                return true;
            }
        }
        return false;
    }

    public static ForbiddenNgramFrequenciesCriteria train(String realText, int ngram) {
        return new ForbiddenNgramFrequenciesCriteria(
                OrderingUtil.tail(
                        FrequencyUtil.frequencies(realText, ngram),
                        StringDoubleComparator.getInstance(),
                        DEFAULT_FORBIDDEN_SIZE),
                ngram
        );
    }

    private static class StringDoubleComparator implements Comparator<Map.Entry<String, Double>> {
        private static final StringDoubleComparator INSTANCE = new StringDoubleComparator();

        private StringDoubleComparator() { }

        @Override
        public int compare(Map.Entry<String, Double> x, Map.Entry<String, Double> y) {
            return Double.compare(x.getValue(), y.getValue());
        }

        static StringDoubleComparator getInstance() {
            return INSTANCE;
        }
    }
}
