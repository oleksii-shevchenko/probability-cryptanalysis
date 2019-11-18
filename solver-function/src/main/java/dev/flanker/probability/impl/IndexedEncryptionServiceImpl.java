package dev.flanker.probability.impl;

import dev.flanker.probability.IndexedEncryptionService;

public class IndexedEncryptionServiceImpl implements IndexedEncryptionService {
    private final int[][] mapping;

    public IndexedEncryptionServiceImpl(int[][] mapping) {
        this.mapping = mapping;
    }

    @Override
    public int encrypt(int messageIndex, int keyIndex) {
        return mapping[messageIndex][keyIndex];
    }
}
