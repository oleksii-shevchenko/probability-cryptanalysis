package dev.flanker.domain;

import dev.flanker.util.ArrayUtil;

public class ConditionalProbabilityDistribution {
    private final double[][] distribution;

    public ConditionalProbabilityDistribution(double[][] distribution) {
        this.distribution = ArrayUtil.copy(distribution);
    }

    public double getConditional(int eventIndex, int conditionIndex) {
        return distribution[eventIndex][conditionIndex];
    }

    public double[][] getDistribution() {
        return ArrayUtil.copy(distribution);
    }
}

