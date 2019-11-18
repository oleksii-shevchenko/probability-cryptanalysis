package dev.flanker.probability;

import dev.flanker.domain.BiProbabilityDistribution;
import dev.flanker.domain.ConditionalProbabilityDistribution;
import dev.flanker.domain.ProbabilityDistribution;

public interface CiphertextProbabilityService {
    ProbabilityDistribution computeAbsolute(ProbabilityDistribution messages, ProbabilityDistribution keys);

    BiProbabilityDistribution computeBiAbsolute(ProbabilityDistribution messages, ProbabilityDistribution keys);

    ConditionalProbabilityDistribution computeConditional(BiProbabilityDistribution mc, ProbabilityDistribution ciphertexts);
}
