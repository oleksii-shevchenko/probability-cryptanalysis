package dev.flanker.util;

public final class ComputeUtil {
    private static final double LN_2 = Math.log(2);

    private ComputeUtil() { }

    static double log(double a) {
        return Math.log(a) / LN_2;
    }

    static int pow(int x, int y) {
        return (int) Math.pow(x, y);
    }

    static String compose(char a, char b) {
        return String.valueOf(a) + b;
    }
}
