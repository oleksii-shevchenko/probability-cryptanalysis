package dev.flanker.util;

import static dev.flanker.util.ComputeUtil.pow;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import dev.flanker.domain.Alphabet;

public final class EncryptionUtil {
    private EncryptionUtil() { }

    public static String vigenereEncryption(String text, String key, Alphabet alphabet) {
        StringBuilder builder = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            int encrypted = (alphabet.code(text.charAt(i)) + alphabet.code(key.charAt(i % key.length()))) % alphabet.size();
            builder.append(alphabet.symbol(encrypted));
        }
        return builder.toString();
    }

    public static String vigenereEncryption(String text, int r, Alphabet alphabet) {
        Random random = ThreadLocalRandom.current();
        StringBuilder key = new StringBuilder(r);
        for (int i = 0; i < r; i++) {
            key.append(alphabet.symbol(random.nextInt(alphabet.size())));
        }
        return vigenereEncryption(text, key.toString(), alphabet);
    }

    public static String affineEncryption(String text, int ngram, int a, int b, Alphabet alphabet) {
        StringBuilder builder = new StringBuilder(text.length());
        int module = pow(alphabet.size(), ngram);
        for (int i = 0; i < text.length(); i += ngram) {
            int c = (a * compress(text, i, ngram, alphabet) + b) % module;
            decompress(builder, c, ngram, alphabet);
        }
        return builder.toString();
    }

    public static String affineEncryption(String text, int ngram, Alphabet alphabet) {
        int a = ThreadLocalRandom.current().nextInt(pow(alphabet.size(), ngram));
        int b = ThreadLocalRandom.current().nextInt(pow(alphabet.size(), ngram));
        return  affineEncryption(text, ngram, a, b, alphabet);
    }

    public static String randomSequence(int size, Alphabet alphabet) {
        Random random = ThreadLocalRandom.current();
        StringBuilder builder = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            builder.append(alphabet.symbol(random.nextInt(alphabet.size())));
        }
        return builder.toString();
    }

    public static String recursiveSequence(int size, int ngram, int x, int y, Alphabet alphabet) {
        StringBuilder builder = new StringBuilder(size);
        int module = pow(alphabet.size(), ngram);

        int s_0 = x;
        int s_1 = y;
        for (int i = 0; i < size; i += ngram) {
            int z = (s_0 + s_1) % module;
            decompress(builder, z, ngram, alphabet);

            s_0 = s_1;
            s_1 = z;
        }
        return builder.toString();
    }

    public static String recursiveSequence(int size, int ngram, Alphabet alphabet) {
        int x = ThreadLocalRandom.current().nextInt(pow(alphabet.size(), ngram));
        int y = ThreadLocalRandom.current().nextInt(pow(alphabet.size(), ngram));
        return recursiveSequence(size, ngram, x, y, alphabet);
    }

    public static String truncate(String text, int divisionFactor) {
        return text.substring(0, text.length() - (text.length() % divisionFactor));
    }

    private static int compress(String text, int offset, int ngram, Alphabet alphabet) {
        int compressed = alphabet.code(text.charAt(offset));
        for (int i = 1; i < ngram; i++) {
            compressed = compressed * alphabet.size() + alphabet.code(text.charAt(offset + i));
        }
        return compressed;
    }

    private static void decompress(StringBuilder builder, int compressed, int ngram, Alphabet alphabet) {
        StringBuilder appending = new StringBuilder(ngram);
        for (int i = 0; i < ngram; i++) {
            appending.append(alphabet.symbol(compressed % alphabet.size()));
            compressed /= alphabet.size();
        }
        builder.append(appending.reverse());
    }
}
