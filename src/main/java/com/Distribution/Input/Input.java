package com.Distribution.Input;

import com.Distribution.Indicator.Indicator;

public class Input {
    private Indicator indicator;

    public Input(Indicator indicator) {
        this.setIndicator(indicator);
    }

    public Indicator getIndicator() {
        return this.indicator;
    }

    private void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    public String toString() {
        return "{\"indicator\": " + this.getIndicator().toString() + "}";
    }
}
