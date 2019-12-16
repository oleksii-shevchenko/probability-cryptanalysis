package dev.flanker.training.impl;

import java.util.HashMap;
import java.util.Map;

import dev.flanker.domain.BiProbabilityDistribution;
import dev.flanker.domain.ConditionalProbabilityDistribution;
import dev.flanker.domain.Config;
import dev.flanker.domain.DeterministicSolverFunction;
import dev.flanker.domain.ProbabilityDistribution;
import dev.flanker.domain.StochasticSolverFunction;
import dev.flanker.probability.ProbabilityService;
import dev.flanker.training.SolverFunctionService;

public class SolverFunctionServiceImpl implements SolverFunctionService {
    public static final double MAX_PROBABILITY_BIAS = 0.001;

    private final ProbabilityService probabilityService;

    public SolverFunctionServiceImpl(ProbabilityService probabilityService) {
        this.probabilityService = probabilityService;
    }

    @Override
    public DeterministicSolverFunction deterministic(ProbabilityDistribution messages, ProbabilityDistribution keys) {
        ProbabilityDistribution ciphertexts = probabilityService.computeAbsolute(messages, keys);
        BiProbabilityDistribution mc = probabilityService.computeBiAbsolute(messages, keys);
        ConditionalProbabilityDistribution conditional = probabilityService.computeConditional(mc, ciphertexts);

        return new DeterministicSolverFunction(buildSimpleMapping(conditional));
    }

    @Override
    public StochasticSolverFunction stochastic(ProbabilityDistribution messages, ProbabilityDistribution keys) {
        ProbabilityDistribution ciphertexts = probabilityService.computeAbsolute(messages, keys);
        BiProbabilityDistribution mc = probabilityService.computeBiAbsolute(messages, keys);
        ConditionalProbabilityDistribution conditional = probabilityService.computeConditional(mc, ciphertexts);

        return new StochasticSolverFunction(buildComplexMapping(conditional));
    }

    private int[] buildSimpleMapping(ConditionalProbabilityDistribution conditionalProbabilityDistribution) {
        int[] mapping = new int[Config.CIPHERTEXT_SPACE_SIZE];
        for (int i = 0; i < Config.CIPHERTEXT_SPACE_SIZE; i++) {
            int maxMessage = 0;
            double maxMessageProbability = -1.0;
            for (int j = 0; j < Config.MESSAGES_SPACE_SIZE; j++) {
                if (conditionalProbabilityDistribution.getConditional(j, i) > maxMessageProbability) {
                    maxMessageProbability = conditionalProbabilityDistribution.getConditional(j, i);
                    maxMessage = j;
                }
            }
            mapping[i] = maxMessage;
        }
        return mapping;
    }

    private double[][] buildComplexMapping(ConditionalProbabilityDistribution conditionalProbabilityDistribution) {
        double[][] mapping = new double[Config.CIPHERTEXT_SPACE_SIZE][Config.MESSAGES_SPACE_SIZE];
        for (int i = 0; i < Config.CIPHERTEXT_SPACE_SIZE; i++) {
            buildRowMapping(mapping[i], i, conditionalProbabilityDistribution);
        }
        return mapping;
    }

    private void buildRowMapping(double[] mapping, int ciphertext, ConditionalProbabilityDistribution conditional) {
        Map<Integer, Double> indexes = new HashMap<>();

        double norm = 0.0;
        double maxProbability = maxConditionalProbability(conditional, ciphertext);
        for (int i = 0; i < Config.MESSAGES_SPACE_SIZE; i++) {
            if (Math.abs(maxProbability - conditional.getConditional(i, ciphertext)) < MAX_PROBABILITY_BIAS) {
                indexes.put(i, conditional.getConditional(i, ciphertext));
                norm += conditional.getConditional(i, ciphertext);
            }
        }
        for (Map.Entry<Integer, Double> entry : indexes.entrySet()) {
            mapping[entry.getKey()] = entry.getValue() / norm;
        }
    }

    private double maxConditionalProbability(ConditionalProbabilityDistribution conditional, int ciphertext) {
        double max = -1.0;
        for (int i = 0; i < Config.MESSAGES_SPACE_SIZE; i++) {
            if (conditional.getConditional(i, ciphertext) > max) {
                max = conditional.getConditional(i, ciphertext);
            }
        }
        return max;
    }
}
