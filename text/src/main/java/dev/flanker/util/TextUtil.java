package dev.flanker.util;

import static dev.flanker.util.ComputeUtil.ENGLISH_ALPHABET_SIZE;
import static dev.flanker.util.ComputeUtil.denormalize;
import static dev.flanker.util.ComputeUtil.normalize;
import static dev.flanker.util.ComputeUtil.pow;

import java.util.concurrent.ThreadLocalRandom;

public final class TextUtil {
    private TextUtil() { }

    public static String sample(String origin, int length) {
        int offset = ThreadLocalRandom.current().nextInt(origin.length() - length);
        return origin.substring(offset, offset + length);
    }
    
    public static String vigenereEncryption(String text, String key) {
        StringBuilder builder = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            int encoded = (normalize(text.charAt(i)) + normalize(key.charAt(i % key.length()))) % ENGLISH_ALPHABET_SIZE;
            builder.append(denormalize(encoded));
        }
        return builder.toString();
    }

    public static String vigenereEncryption(String text, int r) {
        StringBuilder key = new StringBuilder(r);
        for (int i = 0; i < r; i++) {
            key.append(denormalize(ThreadLocalRandom.current().nextInt(ENGLISH_ALPHABET_SIZE)));
        }
        return vigenereEncryption(text, key.toString());
    }

    public static String affineEncryption(String text, int ngram, int a, int b) {
        StringBuilder builder = new StringBuilder(text.length());
        int module = pow(ENGLISH_ALPHABET_SIZE, ngram);
        for (int i = 0; i < text.length(); i += ngram) {
            int c = (a * compress(text, i, ngram) + b) % module;
            decompress(builder, c, ngram);
        }
        return builder.toString();
    }

    public static String affineEncryption(String text, int ngram) {
        int a = ThreadLocalRandom.current().nextInt(pow(ENGLISH_ALPHABET_SIZE, ngram));
        int b = ThreadLocalRandom.current().nextInt(pow(ENGLISH_ALPHABET_SIZE, ngram));
        return  affineEncryption(text, ngram, a, b);
    }

    public static String randomSequence(int size) {
        StringBuilder builder = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            builder.append(denormalize(ThreadLocalRandom.current().nextInt(ENGLISH_ALPHABET_SIZE)));
        }
        return builder.toString();
    }

    public static String recursiveSequence(int size, int ngram, int x, int y) {
        StringBuilder builder = new StringBuilder(size);
        int module = pow(ENGLISH_ALPHABET_SIZE, ngram);

        int s_0 = x;
        int s_1 = y;
        for (int i = 0; i < size; i += ngram) {
            int z = (s_0 + s_1) % module;
            decompress(builder, z, ngram);

            s_0 = s_1;
            s_1 = z;
        }

        return builder.toString();
    }

    public static String recursiveSequence(int size, int ngram) {
        int x = ThreadLocalRandom.current().nextInt(pow(ENGLISH_ALPHABET_SIZE, ngram));
        int y = ThreadLocalRandom.current().nextInt(pow(ENGLISH_ALPHABET_SIZE, ngram));
        return recursiveSequence(size, ngram, x, y);
    }

    public static String truncate(String text, int divisionFactor) {
        return text.substring(0, text.length() - (text.length() % divisionFactor));
    }

    private static int compress(String text, int offset, int ngram) {
        int compressed = normalize(text.charAt(offset));
        for (int i = 1; i < ngram; i++) {
            compressed = compressed * ENGLISH_ALPHABET_SIZE + normalize(text.charAt(offset + i));
        }
        return compressed;
    }

    private static void decompress(StringBuilder builder, int compressed, int ngram) {
        String appending = "";
        for (int i = 0; i < ngram; i++) {
            appending = denormalize((char) (compressed % ENGLISH_ALPHABET_SIZE)) + appending;
            compressed = compressed / ENGLISH_ALPHABET_SIZE;
        }
        builder.append(appending);
    }
}
