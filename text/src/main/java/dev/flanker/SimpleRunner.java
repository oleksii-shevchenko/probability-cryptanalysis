package dev.flanker;

import dev.flanker.criteria.*;
import dev.flanker.util.CsvFormatUtil;
import dev.flanker.util.FileUtil;
import dev.flanker.util.FrequencyUtil;
import dev.flanker.util.TextUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleRunner {
    private static List<Integer> LENGTHS = Arrays.asList(10, 100, 1000, 10000);
    private static int RUNS = 10_000;

    private static Map<Integer, Integer> RUNS_MAP = Map.of(
            10, 10_000,
            100, 10_000,
            1000, 10_000
 //           10_000, 1000,
 //           100_000, 100
    );

    public static void main(String[] args) throws IOException {
        String text = FileUtil.readAndClean("data\\raw.txt");

        Map<String, Double> frequencies = FrequencyUtil.frequencies(text, 1);
        CsvFormatUtil.writeCsv(CsvFormatUtil.formatFrequencies(frequencies), "data\\freq.csv");

        Map<String, Double> bigramFrequencies = FrequencyUtil.frequencies(TextUtil.normalize(text, 2), 2);
        CsvFormatUtil.writeCsv(CsvFormatUtil.formatBigramFrequencies(bigramFrequencies), "data\\bifreq.csv");

        Map<String, TextCriteria> criteria = trainCriteria(text);
        Map<Integer, TextCriteria> correspondence = correspondenceMap(text);
        Map<String, String> textMap = textMap(text);

        for (Map.Entry<Integer, Integer> runConfig : RUNS_MAP.entrySet()) {
            System.out.println(runConfig.getKey() + " Started");

            StringBuilder csv = new StringBuilder("Criteria,");
            for (Map.Entry<String, String> entry : textMap.entrySet()) {
                csv.append(entry.getKey()).append(",");
            }
            replaceLast(csv, '\n');


            for (Map.Entry<String, TextCriteria> criteriaEntry : criteria.entrySet()) {
                csv.append(criteriaEntry.getKey()).append(",");
                for (Map.Entry<String, String> textEntry : textMap.entrySet()) {
                    csv.append(passedRatio(criteriaEntry.getValue(), textEntry.getValue(), runConfig.getKey(), RUNS)).append(",");
                }
                replaceLast(csv, '\n');
                System.out.println(criteriaEntry.getKey() + " Finished");
            }

            TextCriteria textCriteria = correspondence.get(runConfig.getKey());
            csv.append("Correspondence_").append(runConfig.getKey());
            for (Map.Entry<String, String> textEntry : textMap.entrySet()) {
                csv.append(passedRatio(textCriteria, textEntry.getValue(), runConfig.getKey(), runConfig.getValue())).append(",");
            }
            replaceLast(csv, '\n');

            CsvFormatUtil.writeCsv(csv.toString(), "data\\stat_" + runConfig.getKey() + ".csv");

            System.out.println(runConfig.getKey() + " Finished");
        }
    }

    private static void replaceLast(StringBuilder builder, char c) {
        builder.deleteCharAt(builder.length() - 1);
        builder.append(c);
    }

    private static double passedRatio(TextCriteria criteria, String scr, int length, int runs) {
        double counter = 0.0;
        for (int i = 0; i < runs; i++) {
            if (criteria.isRandom(TextUtil.sample(scr, length))) {
                counter++;
            }
        }
        return counter / runs;
    }

    private static Map<String, TextCriteria> trainCriteria(String text) {
        Map<String, TextCriteria> criteriaMap = new HashMap<>();
        criteriaMap.put("Entropy", EntropyCriteria.getCriteria(text, 0.1, 1));
        criteriaMap.put("Entropy Bigram", EntropyCriteria.getCriteria(text, 0.1, 2));
        criteriaMap.put("Extended Forbidden", ExtendedForbiddenNgramCriteria.getCriteria(text, 1, 6, 3));
        criteriaMap.put("Extended Bigram Forbidden", ExtendedForbiddenNgramCriteria.getCriteria(text, 2, 12, 6));
        criteriaMap.put("Extended Forbidden Frequencies", ExtendedForbiddenNgramFrequenciesCriteria.getCriteria(text, 1, 6, 3));
        criteriaMap.put("Extended Forbidden Bigram Frequencies", ExtendedForbiddenNgramFrequenciesCriteria.getCriteria(text, 1, 12, 6));
        criteriaMap.put("Extended Frequent", ExtendedFrequentNgramCriteria.getCriteria(text, 1, 6, 3));
        criteriaMap.put("Extended Bigram Frequent", ExtendedFrequentNgramCriteria.getCriteria(text, 2, 12, 6));
        criteriaMap.put("Extended Frequent Frequencies", ExtendedFrequentNgramFrequenciesCriteria.getCriteria(text, 1, 6, 3));
        criteriaMap.put("Extended Frequent Bigram Frequencies", ExtendedFrequentNgramFrequenciesCriteria.getCriteria(text, 1, 12, 6));
        criteriaMap.put("Forbidden", ForbiddenNgramCriteria.getCriteria(text, 1, 6));
        criteriaMap.put("Forbidden Bigram", ForbiddenNgramCriteria.getCriteria(text, 2, 12));
        criteriaMap.put("Forbidden Frequencies", ForbiddenNgramFrequenciesCriteria.getCriteria(text, 1, 6));
        criteriaMap.put("Forbidden Bigram Frequencies", ForbiddenNgramFrequenciesCriteria.getCriteria(text, 2, 12));
        criteriaMap.put("Frequent", FrequentNgramCriteria.getCriteria(text, 1, 6));
        criteriaMap.put("Frequent Bigram", FrequentNgramCriteria.getCriteria(text, 2, 12));
        criteriaMap.put("Frequent Frequencies", FrequentNgramFrequenciesCriteria.getCriteria(text, 1, 6));
        criteriaMap.put("Frequent Bigram Frequencies", FrequentNgramFrequenciesCriteria.getCriteria(text, 2, 12));
        return criteriaMap;
    }

    private static Map<Integer, TextCriteria> correspondenceMap(String text) {
        Map<Integer, TextCriteria> criteriaMap = new HashMap<>();
        for (Integer length : LENGTHS) {
            criteriaMap.put(length, CorrespondenceCriteria.getCriteria(TextUtil.sample(text, length), 1.0 / length, 2));
        }
        return criteriaMap;
    }

    private static Map<String, String> textMap(String text) {
        Map<String, String> map = new HashMap<>();
        map.put("Origin", text);
        map.put("Vigenere_1", TextUtil.vigenereEncryption(text, 1));
        map.put("Vigenere_5", TextUtil.vigenereEncryption(text, 5));
        map.put("Vigenere_10", TextUtil.vigenereEncryption(text, 10));
        map.put("Affine", TextUtil.affineEncryption(text, 1));
        map.put("Affine_2", TextUtil.affineEncryption(text, 2));
        map.put("Random", TextUtil.randomSequence(text.length()));
        map.put("Recursive", TextUtil.recursiveSequence(text.length(), 1));
        map.put("Recursive_2", TextUtil.recursiveSequence(text.length(), 2));
        return map;
    }
}
