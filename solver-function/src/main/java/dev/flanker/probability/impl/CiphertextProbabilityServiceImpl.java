package dev.flanker.probability.impl;

import dev.flanker.domain.BiProbabilityDistribution;
import dev.flanker.domain.ConditionalProbabilityDistribution;
import dev.flanker.domain.Config;
import dev.flanker.domain.ProbabilityDistribution;
import dev.flanker.probability.CiphertextProbabilityService;
import dev.flanker.probability.IndexedEncryptionService;

public class CiphertextProbabilityServiceImpl implements CiphertextProbabilityService {
    private final IndexedEncryptionService encryptionService;

    public CiphertextProbabilityServiceImpl(IndexedEncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    public ProbabilityDistribution computeAbsolute(ProbabilityDistribution messages, ProbabilityDistribution keys) {
        double[] ciphertexts = new double[Config.CIPHERTEXT_SPACE_SIZE];
        for (int i = 0; i < Config.MESSAGES_SPACE_SIZE; i++) {
            for (int j = 0; j < Config.KEY_SPACE_SIZE; j++) {
                ciphertexts[encryptionService.encrypt(i, j)] += messages.getProbability(i) * keys.getProbability(j);
            }
        }
        return new ProbabilityDistribution(ciphertexts);
    }

    @Override
    public BiProbabilityDistribution computeBiAbsolute(ProbabilityDistribution messages, ProbabilityDistribution keys) {
        double[][] distribution = new double[Config.MESSAGES_SPACE_SIZE][Config.CIPHERTEXT_SPACE_SIZE];
        for (int i = 0; i < Config.MESSAGES_SPACE_SIZE; i++) {
            for (int j = 0; j < Config.KEY_SPACE_SIZE; j++) {
                distribution[i][encryptionService.encrypt(i, j)] += messages.getProbability(i) * keys.getProbability(j);
            }
        }
        return new BiProbabilityDistribution(distribution);
    }

    @Override
    public ConditionalProbabilityDistribution computeConditional(BiProbabilityDistribution mc, ProbabilityDistribution ciphertexts) {
        double[][] conditionalDistribution = new double[Config.MESSAGES_SPACE_SIZE][Config.CIPHERTEXT_SPACE_SIZE];
        for (int i = 0; i < Config.MESSAGES_SPACE_SIZE; i++) {
            for (int j = 0; j < Config.CIPHERTEXT_SPACE_SIZE; j++) {
                if (ciphertexts.getProbability(j) != 0) {
                    conditionalDistribution[i][j] = mc.getProbability(i, j) / ciphertexts.getProbability(j);
                }
            }
        }
        return new ConditionalProbabilityDistribution(conditionalDistribution);
    }
}
