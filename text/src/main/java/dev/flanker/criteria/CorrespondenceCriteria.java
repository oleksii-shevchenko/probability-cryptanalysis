package dev.flanker.criteria;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import dev.flanker.domain.Alphabet;
import dev.flanker.util.FileUtil;
import dev.flanker.util.TextUtil;

public class CorrespondenceCriteria implements TextCriteria {
    private final Map<Integer, Double> correspondenceIndex;
    private final Map<Integer, Double> bias;
    private final Alphabet alphabet;
    private final int ngram;

    private CorrespondenceCriteria(Map<Integer, Double> correspondenceIndex, Map<Integer, Double> bias, Alphabet alphabet, int ngram) {
        this.correspondenceIndex = correspondenceIndex;
        this.bias = bias;
        this.alphabet = alphabet;
        this.ngram = ngram;
    }

    @Override
    public boolean isRandom(String text) {
        int length = text.length();
        if (correspondenceIndex.containsKey(length)) {
            return Math.abs(TextUtil.correspondenceIndex(text, ngram, alphabet) - correspondenceIndex.get(length)) > bias.get(length);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static CorrespondenceCriteria getCriteria(String text, int ngram, Map<Integer, Double> bias, Alphabet alphabet) {
        Map<Integer, Double> correspondenceIndex = bias.keySet()
                .stream()
                .collect(Collectors.toMap(key -> key, key -> TextUtil.correspondenceIndex(FileUtil.sample(text, key), ngram, alphabet)));
        return new CorrespondenceCriteria(correspondenceIndex, new HashMap<>(bias), alphabet, ngram);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CorrespondenceCriteria.class.getSimpleName() + "[", "]")
                .add("correspondenceIndex=" + correspondenceIndex)
                .add("bias=" + bias)
                .add("ngram=" + ngram)
                .toString();
    }
}
