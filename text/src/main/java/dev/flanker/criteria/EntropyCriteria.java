package dev.flanker.criteria;

import java.util.StringJoiner;

import dev.flanker.domain.Alphabet;
import dev.flanker.util.TextUtil;

public class EntropyCriteria implements TextCriteria {
    private final Alphabet alphabet;
    private final double entropy;
    private final double bias;
    private final int ngram;

    private EntropyCriteria(Alphabet alphabet, double entropy, double bias, int ngram) {
        this.alphabet = alphabet;
        this.entropy = entropy;
        this.bias = bias;
        this.ngram = ngram;
    }

    @Override
    public boolean isRandom(String text) {
        return Math.abs(TextUtil.entropy(text, ngram, alphabet) - entropy) > bias;
    }

    public static EntropyCriteria getCriteria(String text, double bias, int ngram, Alphabet alphabet) {
        return new EntropyCriteria(alphabet, TextUtil.entropy(text, ngram, alphabet), bias, ngram);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EntropyCriteria.class.getSimpleName() + "[", "]")
                .add("entropy=" + entropy)
                .add("bias=" + bias)
                .add("ngram=" + ngram)
                .toString();
    }
}
