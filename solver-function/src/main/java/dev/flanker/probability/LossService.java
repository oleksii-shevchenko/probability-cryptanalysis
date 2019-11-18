package dev.flanker.probability;

import dev.flanker.domain.BiProbabilityDistribution;
import dev.flanker.domain.DeterministicSolverFunction;
import dev.flanker.domain.StochasticSolverFunction;

public interface LossService {
    double deterministicLossValue(DeterministicSolverFunction function, BiProbabilityDistribution mc);

    double stochasticLossValue(StochasticSolverFunction function, BiProbabilityDistribution mc);
}
