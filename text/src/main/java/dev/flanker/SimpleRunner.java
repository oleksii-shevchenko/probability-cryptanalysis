package dev.flanker;

import static dev.flanker.util.FormatUtil.formatBigramFrequencies;
import static dev.flanker.util.FormatUtil.formatFrequencies;
import static dev.flanker.util.TextUtil.frequencies;

import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import dev.flanker.criteria.CorrespondenceCriteria;
import dev.flanker.criteria.EntropyCriteria;
import dev.flanker.criteria.ExtendedForbiddenNgramCriteria;
import dev.flanker.criteria.ExtendedForbiddenNgramFrequenciesCriteria;
import dev.flanker.criteria.ExtendedFrequentNgramCriteria;
import dev.flanker.criteria.ExtendedFrequentNgramFrequenciesCriteria;
import dev.flanker.criteria.ForbiddenNgramCriteria;
import dev.flanker.criteria.ForbiddenNgramFrequenciesCriteria;
import dev.flanker.criteria.FrequentNgramCriteria;
import dev.flanker.criteria.FrequentNgramFrequenciesCriteria;
import dev.flanker.criteria.TextCriteria;
import dev.flanker.domain.Alphabet;
import dev.flanker.util.EncryptionUtil;
import dev.flanker.util.FileUtil;
import dev.flanker.util.FormatUtil;

public class SimpleRunner {
    private static final int GCD = 2;

    private static final String ORIGIN_TEXT_KEY = "Origin";

    private static Map<Integer, Integer> RUNS_CONFIGURATION = Map.of(
            10, 10_000,
            100, 1000,
            1_000, 1000,
            10_000, 100,
            100_000, 10
    );

    private static final Map<Integer, Double> CORRESPONDENCE_CRITERIA_BIAS_MAP = Map.of(
            10, Math.pow(0.1, 6),
            100, Math.pow(0.05, 6),
            1_000, Math.pow(0.1, 9),
            10_000, Math.pow(0.1, 11),
            100_000, Math.pow(0.1, 12)
    );


    public static void main(String[] args) {
        Alphabet alphabet = Alphabet.CYRILLIC_ALPHABET;

        String content = FileUtil.read(Paths.get("data/raw-cyrillic.txt"));
        String clean = FileUtil.cleanCyrillic(content);
        String origin = EncryptionUtil.truncate(clean, GCD);

        FileUtil.write(origin, Paths.get("data/cleaned-cyrillic.txt"));
        FileUtil.write(formatFrequencies(frequencies(origin, 1, alphabet)), Paths.get("data\\freq.csv"));
        FileUtil.write(formatBigramFrequencies(frequencies(origin, 2, alphabet), alphabet), Paths.get("data\\bifreq.csv"));

        Map<String, TextCriteria> criteria = prepareCriteria(origin, alphabet);
        Map<String, String> text = prepareText(origin, alphabet);

        StringBuilder csv = header(criteria);
        for (Map.Entry<String, String> textEntry : text.entrySet()) {
            for (Map.Entry<Integer, Integer> configEntry : RUNS_CONFIGURATION.entrySet()) {
                csv.append(textEntry.getKey())
                        .append(",")
                        .append(configEntry.getKey())
                        .append(",")
                        .append(errorType(textEntry.getKey()))
                        .append(",");

                for (Map.Entry<String, TextCriteria> criteriaEntry : criteria.entrySet()) {
                    double probability = randomRatio(criteriaEntry.getValue(), textEntry.getValue(), configEntry.getKey(), configEntry.getValue());
                    if (ORIGIN_TEXT_KEY.equals(textEntry.getKey())) {
                        csv.append(probability);
                    } else {
                        csv.append(1.0 - probability);
                    }
                    csv.append(",");
                }

                replaceLast(csv, '\n');
            }
        }

        FileUtil.write(csv.toString(), Paths.get("data/results.csv"));
        FileUtil.write(FormatUtil.formatCriteriaConfig(criteria), Paths.get("data/criteria-config.txt"));
    }

    private static double randomRatio(TextCriteria criteria, String scr, int length, int runs) {
        double counter = 0.0;
        for (int i = 0; i < runs; i++) {
            if (criteria.isRandom(FileUtil.sample(scr, length))) {
                counter++;
            }
        }
        return counter / runs;
    }

    private static Map<String, TextCriteria> prepareCriteria(String text, Alphabet alphabet) {
        Map<String, TextCriteria> criteriaMap = new LinkedHashMap<>();

        criteriaMap.put("Entropy",
                EntropyCriteria.getCriteria(text, 0.35, 1, alphabet));
        criteriaMap.put("Entropy Bigram",
                EntropyCriteria.getCriteria(text, 0.25, 2, alphabet));
        criteriaMap.put("Extended Forbidden",
                ExtendedForbiddenNgramCriteria.getCriteria(text, 1, 6, 2, alphabet));
        criteriaMap.put("Extended Bigram Forbidden",
                ExtendedForbiddenNgramCriteria.getCriteria(text, 2, 12, 6, alphabet));
        criteriaMap.put("Extended Forbidden Frequencies",
                ExtendedForbiddenNgramFrequenciesCriteria.getCriteria(text, 1, 6, 3, alphabet));
        criteriaMap.put("Extended Forbidden Bigram Frequencies",
                ExtendedForbiddenNgramFrequenciesCriteria.getCriteria(text, 1, 12, 6, alphabet));
        criteriaMap.put("Extended Frequent",
                ExtendedFrequentNgramCriteria.getCriteria(text, 1, 6, 3, alphabet));
        criteriaMap.put("Extended Bigram Frequent",
                ExtendedFrequentNgramCriteria.getCriteria(text, 2, 12, 6, alphabet));
        criteriaMap.put("Extended Frequent Frequencies",
                ExtendedFrequentNgramFrequenciesCriteria.getCriteria(text, 1, 6, 3, alphabet));
        criteriaMap.put("Extended Frequent Bigram Frequencies",
                ExtendedFrequentNgramFrequenciesCriteria.getCriteria(text, 1, 12, 6, alphabet));
        criteriaMap.put("Forbidden",
                ForbiddenNgramCriteria.getCriteria(text, 1, 6, alphabet));
        criteriaMap.put("Forbidden Bigram",
                ForbiddenNgramCriteria.getCriteria(text, 2, 12, alphabet));
        criteriaMap.put("Forbidden Frequencies",
                ForbiddenNgramFrequenciesCriteria.getCriteria(text, 1, 6, alphabet));
        criteriaMap.put("Forbidden Bigram Frequencies",
                ForbiddenNgramFrequenciesCriteria.getCriteria(text, 2, 12, alphabet));
        criteriaMap.put("Frequent",
                FrequentNgramCriteria.getCriteria(text, 1, 6, alphabet));
        criteriaMap.put("Frequent Bigram",
                FrequentNgramCriteria.getCriteria(text, 2, 12, alphabet));
        criteriaMap.put("Frequent Frequencies",
                FrequentNgramFrequenciesCriteria.getCriteria(text, 1, 6, alphabet));
        criteriaMap.put("Frequent Bigram Frequencies",
                FrequentNgramFrequenciesCriteria.getCriteria(text, 2, 12, alphabet));
        criteriaMap.put("Correspondence",
                CorrespondenceCriteria.getCriteria(text, 1, CORRESPONDENCE_CRITERIA_BIAS_MAP, alphabet));
        criteriaMap.put("Correspondence Bigram",
                CorrespondenceCriteria.getCriteria(text, 2, CORRESPONDENCE_CRITERIA_BIAS_MAP, alphabet));

        return criteriaMap;
    }

    private static Map<String, String> prepareText(String text, Alphabet alphabet) {
        Map<String, String> map = new LinkedHashMap<>();

        map.put("Origin", text);
        map.put("Vigenere (1)", EncryptionUtil.vigenereEncryption(text, 1, alphabet));
        map.put("Vigenere (5)", EncryptionUtil.vigenereEncryption(text, 5, alphabet));
        map.put("Vigenere (10)", EncryptionUtil.vigenereEncryption(text, 10, alphabet));
        map.put("Affine", EncryptionUtil.affineEncryption(text, 1, alphabet));
        map.put("Affine Bigram", EncryptionUtil.affineEncryption(text, 2, alphabet));
        map.put("Random", EncryptionUtil.randomSequence(text.length(), alphabet));
        map.put("Recursive", EncryptionUtil.recursiveSequence(text.length(), 1, alphabet));
        map.put("Recursive Bigram", EncryptionUtil.recursiveSequence(text.length(), 2, alphabet));

        return map;
    }

    private static String errorType(String textKey) {
        if (ORIGIN_TEXT_KEY.equals(textKey)) {
            return "False positive";
        } else {
            return "False negative";
        }
    }

    private static StringBuilder header(Map<String, TextCriteria> criteria) {
        StringBuilder csv = new StringBuilder("Source,Length,Error,");
        for (Map.Entry<String, TextCriteria> criteriaEntry : criteria.entrySet()) {
            csv.append(criteriaEntry.getKey()).append(",");
        }
        replaceLast(csv, '\n');
        return csv;
    }

    private static void replaceLast(StringBuilder builder, char c) {
        builder.deleteCharAt(builder.length() - 1);
        builder.append(c);
    }
}
