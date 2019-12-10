package dev.flanker.util;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;

import dev.flanker.domain.BiProbabilityDistribution;
import dev.flanker.domain.ConditionalProbabilityDistribution;
import dev.flanker.domain.Config;
import dev.flanker.domain.DeterministicSolverFunction;
import dev.flanker.domain.ProbabilityDistribution;
import dev.flanker.domain.StochasticSolverFunction;
import dev.flanker.probability.IndexedEncryptionService;
import dev.flanker.probability.impl.IndexedEncryptionServiceImpl;

public final class CsvUtil {
    private CsvUtil() { }

    public static String readFile(String path) {
        try {
            return IOUtils.toString(new FileInputStream(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void writeFile(String content, String path) {
        try (Writer writer = new BufferedWriter(new FileWriter(path))) {
            IOUtils.write(content, writer);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static ProbabilityDistribution parseMessagesProbability(String csv) {
        double[] probabilities = Arrays.stream(csv.split("\n")[0].split(","))
                .mapToDouble(Double::parseDouble)
                .toArray();
        return new ProbabilityDistribution(probabilities);
    }

    public static ProbabilityDistribution parseKeysProbability(String csv) {
        double[] probabilities = Arrays.stream(csv.split("\n")[1].split(","))
                .mapToDouble(Double::parseDouble)
                .toArray();
        return new ProbabilityDistribution(probabilities);
    }

    public static IndexedEncryptionService parseEncryption(String csv) {
        String[] rows = csv.split("\n");
        String[][] table = new String[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            table[i] = rows[i].split(",");
        }

        int[][] encryption = new int[Config.MESSAGES_SPACE_SIZE][Config.KEY_SPACE_SIZE];
        for (int i = 0; i < Config.MESSAGES_SPACE_SIZE; i++) {
            for (int j = 0; j < Config.KEY_SPACE_SIZE; j++) {
                encryption[i][j] = Integer.parseInt(table[j][i]);
            }
        }

        return new IndexedEncryptionServiceImpl(encryption);
    }

    public static String formatFunction(DeterministicSolverFunction function) {
        StringBuilder csv = new StringBuilder("C,M\n");
        int[] mapping = function.getMapping();
        for (int i = 0; i < mapping.length; i++) {
            csv.append(i)
                    .append(",")
                    .append(mapping[i])
                    .append("\n");
        }
        return csv.toString();
    }

    public static String formatFunction(StochasticSolverFunction function) {
        return formatTable(function.getMapping(), "C", "M");
    }

    public static String formatBiAbsolute(BiProbabilityDistribution distribution, String row, String col) {
        return formatTable(distribution.getDistribution(), row, col);
    }

    public static String formatConditional(ConditionalProbabilityDistribution distribution, String row, String col) {
        return formatTable(distribution.getDistribution(), row, col);
    }

    public static String formatAbsolute(ProbabilityDistribution distribution) {
        StringBuilder csv = new StringBuilder("C,Pr\n");
        double[] probabilities = distribution.getDistribution();
        for (int i = 0; i < probabilities.length; i++) {
            csv.append(i)
                    .append(",")
                    .append(probabilities[i])
                    .append("\n");
        }
        return csv.toString();
    }

    private static String formatTable(double[][] table, String row, String col) {
        StringBuilder csv = new StringBuilder();
        csv.append(row).append("\\").append(col).append(",");
        for (int i = 0; i < table.length; i++) {
            csv.append(i);
            if (i != table.length - 1) {
                csv.append(",");
            } else {
                csv.append("\n");
            }
        }

        for (int i = 0; i < table.length; i++) {
            csv.append(i).append(",");
            for (int j = 0; j < table[i].length; j++) {
                csv.append(table[i][j]);
                if (j != table[i].length - 1) {
                    csv.append(",");
                } else {
                    csv.append("\n");
                }
            }
        }

        return csv.toString();
    }
}
