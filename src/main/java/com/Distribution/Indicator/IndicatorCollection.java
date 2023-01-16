package com.Distribution.Indicator;

public class IndicatorCollection {
    private Indicator[] indicators;

    public IndicatorCollection(Indicator[] indicators) {
        this.indicators = indicators;
    }

    public Indicator getAverageIndicator() {
        Indicator averageIndicator = new Indicator();
        int specialTasks = 0;

        for (Indicator indicator : this.indicators) {
            averageIndicator.setRoughness(averageIndicator.getRoughness() + indicator.getRoughness());
            averageIndicator.setTime(averageIndicator.getTime() + indicator.getTime());
            averageIndicator.setCost(averageIndicator.getCost() + indicator.getCost());
            averageIndicator.setPeople(averageIndicator.getPeople() + indicator.getPeople());
            specialTasks += indicator.isSpecial() ? 1 : 0;
        }

        averageIndicator.setRoughness(averageIndicator.getRoughness() / this.indicators.length);
        averageIndicator.setTime(averageIndicator.getTime() / this.indicators.length);
        averageIndicator.setCost(averageIndicator.getCost() / this.indicators.length);
        averageIndicator.setPeople(averageIndicator.getPeople() / this.indicators.length);
        averageIndicator.setSpecial(specialTasks > 0);

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
