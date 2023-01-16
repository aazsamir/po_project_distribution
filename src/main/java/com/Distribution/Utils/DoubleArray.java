package com.Distribution.Utils;

public class DoubleArray {
    public static Double getAverage(Double[] array) {
        if (array.length == 0) {
            return 0.0;
        }

        Double sum = 0.0;

        for (Double value : array) {
            sum += value;
        }

        return sum / array.length;
    }

    public static Double getSum(Double[] array) {
        Double sum = 0.0;

        for (Double value : array) {
            sum += value;
        }

        return sum;
    }
}
