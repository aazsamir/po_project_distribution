package com.Distribution.Indicator;

public class IndicatorCollection {
    private Indicator[] indicators;

    public IndicatorCollection(Indicator[] indicators) {
        this.indicators = indicators;
    }

    public Indicator getAverageIndicator() {
        Indicator averageIndicator = new Indicator();

        for (Indicator indicator : this.indicators) {
            averageIndicator.setValue(averageIndicator.getValue() + indicator.getValue());
        }

        averageIndicator.setValue(averageIndicator.getValue() / this.indicators.length);

        return averageIndicator;
    }

    public String toString() {
        String string = "{\"indicators\": [";

        for (Indicator indicator : this.indicators) {
            string += indicator.toString() + ", ";
        }

        string = string.substring(0, string.length() - 2);
        string += "]}";

        return string;
    }
}
