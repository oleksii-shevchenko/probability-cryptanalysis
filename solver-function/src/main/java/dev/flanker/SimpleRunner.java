package dev.flanker;

import dev.flanker.domain.BiProbabilityDistribution;
import dev.flanker.domain.ConditionalProbabilityDistribution;
import dev.flanker.domain.DeterministicSolverFunction;
import dev.flanker.domain.ProbabilityDistribution;
import dev.flanker.domain.StochasticSolverFunction;
import dev.flanker.probability.ProbabilityService;
import dev.flanker.probability.IndexedEncryptionService;
import dev.flanker.probability.LossService;
import dev.flanker.probability.impl.ProbabilityServiceImpl;
import dev.flanker.probability.impl.LossServiceImpl;
import dev.flanker.training.SolverFunctionService;
import dev.flanker.training.impl.SolverFunctionServiceImpl;
import dev.flanker.util.CsvUtil;

public class SimpleRunner {
    public static void main(String[] args) {
        String distributionCsv = CsvUtil.readFile("data\\distribution.csv");
        String encryptionCsv = CsvUtil.readFile("data\\encryption.csv");

        ProbabilityDistribution messagesDistribution = CsvUtil.parseMessagesProbability(distributionCsv);
        ProbabilityDistribution keysDistribution = CsvUtil.parseKeysProbability(distributionCsv);
        IndexedEncryptionService encryptionService = CsvUtil.parseEncryption(encryptionCsv);

        ProbabilityService probabilityService = new ProbabilityServiceImpl(encryptionService);
        SolverFunctionService solverFunctionService = new SolverFunctionServiceImpl(probabilityService);
        LossService lossService = new LossServiceImpl();

        ProbabilityDistribution ciphertextsDistribution = probabilityService.computeAbsolute(messagesDistribution, keysDistribution);
        BiProbabilityDistribution mc = probabilityService.computeBiAbsolute(messagesDistribution, keysDistribution);
        ConditionalProbabilityDistribution conditional = probabilityService.computeConditional(mc, ciphertextsDistribution);

        DeterministicSolverFunction deterministic = solverFunctionService.deterministic(messagesDistribution, keysDistribution);
        StochasticSolverFunction stochastic = solverFunctionService.stochastic(messagesDistribution, keysDistribution);

        double deterministicLossValue = lossService.deterministicLossValue(deterministic, mc);
        double stochasticLossValue = lossService.stochasticLossValue(stochastic, mc);

        String lossValues = "deterministicLossValue = " + deterministicLossValue + "\n"
                + "stochasticLossValue = " + stochasticLossValue;

        CsvUtil.writeFile(CsvUtil.formatAbsolute(ciphertextsDistribution), "data\\chipertextsDistribution.csv");
        CsvUtil.writeFile(CsvUtil.formatBiAbsolute(mc, "M", "C"), "data\\mc.csv");
        CsvUtil.writeFile(CsvUtil.formatConditional(conditional, "M", "C"), "data\\m-condition-c.csv");
        CsvUtil.writeFile(CsvUtil.formatFunction(deterministic), "data\\deterministic.csv");
        CsvUtil.writeFile(CsvUtil.formatFunction(stochastic), "data\\stochastic.csv");
        CsvUtil.writeFile(lossValues, "data\\loss-values.txt");
    }
}
