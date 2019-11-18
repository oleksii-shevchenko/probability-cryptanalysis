package dev.flanker.domain;

import dev.flanker.util.ArrayUtil;

public class ProbabilityDistribution {
    private final double[] distribution;

    public ProbabilityDistribution(double[] distribution) {
        this.distribution = ArrayUtil.copy(distribution);
    }

    public double getProbability(int index) {
        return distribution[index];
    }
}
