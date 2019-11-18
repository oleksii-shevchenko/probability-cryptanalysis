package dev.flanker.training;

import dev.flanker.domain.DeterministicSolverFunction;
import dev.flanker.domain.ProbabilityDistribution;
import dev.flanker.domain.StochasticSolverFunction;

public interface SolverFunctionService {
    DeterministicSolverFunction deterministic(ProbabilityDistribution messages, ProbabilityDistribution keys);

    StochasticSolverFunction stochastic(ProbabilityDistribution messages, ProbabilityDistribution keys);
}
