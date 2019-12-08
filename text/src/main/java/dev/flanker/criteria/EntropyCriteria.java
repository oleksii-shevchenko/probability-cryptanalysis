package dev.flanker.criteria;

import dev.flanker.util.FrequencyUtil;

public class EntropyCriteria implements TextCriteria {
    private final double entropy;
    private final double bias;
    private final int ngram;

    private EntropyCriteria(double entropy, double bias, int ngram) {
        this.entropy = entropy;
        this.bias = bias;
        this.ngram = ngram;
    }

    @Override
    public boolean isRandom(String text) {
        return Math.abs(FrequencyUtil.entropy(text, ngram) - entropy) > bias;
    }

    public static EntropyCriteria getCriteria(String text, double bias, int ngram) {
        return new EntropyCriteria(FrequencyUtil.entropy(text, ngram), bias, ngram);
    }
}
