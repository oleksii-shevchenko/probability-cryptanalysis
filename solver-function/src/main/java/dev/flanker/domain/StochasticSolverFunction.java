package dev.flanker.domain;

import dev.flanker.util.ArrayUtil;

import java.util.Random;

public class StochasticSolverFunction {
    private final double[][] stochasticMapping;

    private final Random random;

    public StochasticSolverFunction(double[][] stochasticMapping) {
        this(stochasticMapping, new Random());
    }

    public StochasticSolverFunction(double[][] stochasticMapping, Random random) {
        this.stochasticMapping = ArrayUtil.copy(stochasticMapping);
        this.random = random;
    }

    public int solve(int ciphertextIndex) {
        return chooseRandom(stochasticMapping[ciphertextIndex], random.nextDouble());
    }

    public double value(int ciphertext, int message) {
        return stochasticMapping[ciphertext][message];
    }

    private int chooseRandom(double[] distribution, double coin) {
        double accumulator = 0.0;
        for (int i = 0; i < Config.MESSAGES_SPACE_SIZE; i++) {
            accumulator += distribution[i];
            if (accumulator > coin) {
                return i;
            }
        }
        return Config.MESSAGES_SPACE_SIZE - 1;
    }

    public double[][] getMapping() {
        return ArrayUtil.copy(stochasticMapping);
    }
}
