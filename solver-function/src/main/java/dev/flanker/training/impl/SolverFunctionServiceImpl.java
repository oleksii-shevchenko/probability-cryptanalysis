package dev.flanker.training.impl;

import dev.flanker.domain.*;
import dev.flanker.probability.CiphertextProbabilityService;
import dev.flanker.training.SolverFunctionService;

import java.util.ArrayList;
import java.util.List;

public class SolverFunctionServiceImpl implements SolverFunctionService {
    public static final double MAX_PROBABILITY_BIAS = 0.005;

    private final CiphertextProbabilityService probabilityService;

    public SolverFunctionServiceImpl(CiphertextProbabilityService probabilityService) {
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
        List<Integer> indexes = new ArrayList<>();

        double maxProbability = maxConditionalProbability(conditional, ciphertext);
        for (int i = 0; i < Config.MESSAGES_SPACE_SIZE; i++) {
            if (Math.abs(maxProbability - conditional.getConditional(i, ciphertext)) < MAX_PROBABILITY_BIAS) {
                indexes.add(i);
            }
        }

        for (Integer index : indexes) {
            mapping[index] = (1.0 / indexes.size());
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
