package dev.flanker.util;

public final class ComputeUtil {
    private static final double LN_2 = Math.log(2);

    public static final int ENGLISH_ALPHABET_SIZE = 26;

    public static final int A_SHIFT = 97;

    private ComputeUtil() { }

    public static double log(double a) {
        return Math.log(a) / LN_2;
    }

    public static int pow(int x, int y) {
        return (int) Math.pow(x, y);
    }

    public static String compose(int a, int b) {
        char x = (char) (a + A_SHIFT);
        char y = (char) (b + A_SHIFT);
        return "" + x + y;
    }

    public static int normalize(int c) {
        return (c - A_SHIFT) % ENGLISH_ALPHABET_SIZE;
    }

    public static char denormalize(int c) {
        return (char) ((c % ENGLISH_ALPHABET_SIZE) + A_SHIFT);
    }

    public static String denormalizeString(int c) {
        return "" + denormalize(c);
    }
}
