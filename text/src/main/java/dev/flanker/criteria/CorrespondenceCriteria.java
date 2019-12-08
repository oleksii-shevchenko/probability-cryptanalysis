package dev.flanker.criteria;

import dev.flanker.util.FrequencyUtil;

public class CorrespondenceCriteria implements TextCriteria {
    private final double correspondenceIndex;
    private final double bias;
    private final int ngram;

    private CorrespondenceCriteria(double correspondenceIndex, double bias, int ngram) {
        this.correspondenceIndex = correspondenceIndex;
        this.bias = bias;
        this.ngram = ngram;
    }

    @Override
    public boolean isRandom(String text) {
        return Math.abs(FrequencyUtil.correspondenceIndex(text, ngram) - correspondenceIndex) > bias;
    }

    public static CorrespondenceCriteria getCriteria(String text, double bias, int ngram) {
        return new CorrespondenceCriteria(FrequencyUtil.correspondenceIndex(text, ngram), bias, ngram);
    }

    @Override
    public String toString() {
        return "CorrespondenceCriteria{" +
                "correspondenceIndex=" + correspondenceIndex +
                ", bias=" + bias +
                ", ngram=" + ngram +
                '}';
    }
}
