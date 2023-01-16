package com.Distribution.Utils;

public class Clamp {
    public static Double clamp(Double value, Double min, Double max) {
        if (value < min) {
            value = min;
        } else if (value > max) {
            value = max;
        }

        return value;
    }
}
