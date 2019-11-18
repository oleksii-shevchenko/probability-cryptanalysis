package dev.flanker.util;

import java.lang.reflect.Array;
import java.util.Arrays;

public final class ArrayUtil {
    private ArrayUtil() { }

    public static int[] copy(int[] src) {
        return Arrays.copyOf(src, src.length);
    }

    public static double[] copy(double[] src) {
        return Arrays.copyOf(src, src.length);
    }

    public static double[][] copy(double[][] src) {
        double[][] dst = new double[src.length][];
        for (int i = 0; i < src.length; i++) {
            dst[i] = copy(src[i]);
        }
        return dst;
    }
}
