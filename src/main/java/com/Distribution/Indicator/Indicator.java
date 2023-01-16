package com.Distribution.Indicator;

public class Indicator {
    private Double value;

    public Indicator() {
        this.setValue(0.0);
    }

    public Indicator(Double value) {
        this.setValue(value);
    }

    public Double getValue() {
        return this.value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String toString() {
        return "{\"value\": " + this.getValue() + "}";
    }
}
