package dev.flanker.domain;

import dev.flanker.util.ArrayUtil;

public class DeterministicSolverFunction {
    private final int[] mapping;

    public DeterministicSolverFunction(int[] mapping) {
        this.mapping = ArrayUtil.copy(mapping);
    }

    public int solve(int ciphertextIndex) {
        return mapping[ciphertextIndex];
    }

    public int[] getMapping() {
        return ArrayUtil.copy(mapping);
    }
}
