package dev.flanker.probability.impl;

import dev.flanker.domain.BiProbabilityDistribution;
import dev.flanker.domain.Config;
import dev.flanker.domain.DeterministicSolverFunction;
import dev.flanker.domain.StochasticSolverFunction;
import dev.flanker.probability.LossService;

public class LossServiceImpl implements LossService {
    @Override
    public double deterministicLossValue(DeterministicSolverFunction function, BiProbabilityDistribution mc) {
        double loss = 0.0;
        for (int i = 0; i < Config.MESSAGES_SPACE_SIZE; i++) {
            for (int j = 0; j < Config.CIPHERTEXT_SPACE_SIZE; j++) {
                loss += (mc.getProbability(i, j) * deterministicLoss(function, j, i));
            }
        }
        return loss;
    }

    @Override
    public double stochasticLossValue(StochasticSolverFunction function, BiProbabilityDistribution mc) {
        double loss = 0.0;
        for (int i = 0; i < Config.MESSAGES_SPACE_SIZE; i++) {
            for (int j = 0; j < Config.CIPHERTEXT_SPACE_SIZE; j++) {
                loss += (mc.getProbability(i, j) * stochasticLoss(function, j, i));
            }
        }
        return loss;
    }

    private double stochasticLoss(StochasticSolverFunction function, int ciphertext, int message) {
        double value = 0.0;
        for (int i = 0; i < Config.MESSAGES_SPACE_SIZE; i++) {
            if (i != message) {
                value += function.value(ciphertext, i);
            }
        }
        return value;
    }

    private double deterministicLoss(DeterministicSolverFunction function, int ciphertext, int message) {
        if (function.solve(ciphertext) != message) {
            return 1.0;
        } else {
            return 0.0;
        }
    }
}
