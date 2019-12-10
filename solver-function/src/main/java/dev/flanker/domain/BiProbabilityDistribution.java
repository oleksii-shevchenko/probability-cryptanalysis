package dev.flanker.domain;

import dev.flanker.util.ArrayUtil;

public class BiProbabilityDistribution {
    private final double[][] distribution;

    public BiProbabilityDistribution(double[][] distribution) {
        this.distribution = ArrayUtil.copy(distribution);
    }

    public double getProbability(int x, int y) {
        return distribution[x][y];
    }

    public double[][] getDistribution() {
        return ArrayUtil.copy(distribution);
    }
}
