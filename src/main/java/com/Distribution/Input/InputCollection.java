package com.Distribution.Input;

import java.util.Vector;
import java.util.function.Function;

import com.Distribution.Indicator.Indicator;
import com.Distribution.Indicator.IndicatorCollection;

public class InputCollection {
    private Vector<Input> inputs;

    public InputCollection(Input[] inputs) {
        this.setInputs(inputs);
    }

    public InputCollection filterByTargetId(Integer idTarget) {
        Input[] inputs = new Input[getSize()];
        int i = 0;

        for (Input input : getInputs()) {
            if (input.getIdTarget() == idTarget) {
                inputs[i] = input;
                i++;
            }
        }

        return new InputCollection(inputs);
    }

    public Input[] getInputs() {
        Input[] inputs = new Input[this.inputs.size()];

        for (int i = 0; i < this.inputs.size(); i++) {
            inputs[i] = this.inputs.get(i);
        }

        return inputs;
    }

    public Input getInput(int index) {
        return this.inputs.get(index);
    }

    private void setInputs(Input[] inputs) {
        this.inputs = new Vector<Input>();

        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] != null) {
                this.inputs.add(inputs[i]);
            }
        }
    }

    public int getSize() {
        return this.inputs.size();
    }

    public Indicator getAverageIndicator() {
        Indicator[] indicators = new Indicator[this.getSize()];

        for (int i = 0; i < this.getSize(); i++) {
            indicators[i] = this.inputs.get(i).getIndicator();
        }

        return new IndicatorCollection(indicators).getAverageIndicator();
    }

    public void addInput(Input input) {
        this.inputs.add(input);
    }

    public void pop() {
        this.inputs.remove(this.getSize() - 1);
    }

    public void remove(int i) {
        this.inputs.remove(i);
    }

    public Double getNormalizedRoughness(Double value) {
        return this.getNormalizedValue(value, (indicator) -> indicator.getRoughness());
    }

    public Double getNormalizedTime(Double value) {
        return this.getNormalizedValue(value, (indicator) -> indicator.getTime());
    }

    public Double getNormalizedCost(Double value) {
        return this.getNormalizedValue(value, (indicator) -> indicator.getCost());
    }

    public Double getNormalizedPeople(Double value) {
        return this.getNormalizedValue(value, (indicator) -> indicator.getPeople());
    }

    public Double getNormalizedSpecial(Double value) {
        return this.getNormalizedValue(value, (indicator) -> indicator.isSpecial() ? 1.0 : 0.0);
    }

    private Double getNormalizedValue(Double value, Function<Indicator, Double> callback) {
        Double min = null;
        Double max = null;
        Double indicatorValue;

        for (Input input : this.inputs) {
            indicatorValue = callback.apply(input.getIndicator());

            if (min == null || indicatorValue < min) {
                min = indicatorValue;
            }

            if (max == null || indicatorValue > max) {
                max = indicatorValue;
            }
        }

        return (max - min) > 0 ? (value - min) / (max - min) : 0.0;
    }

    public String toString() {
        String string = "{\"inputs\": [";

        for (int i = 0; i < this.getSize(); i++) {
            string += this.inputs.get(i).toString();

            if (i < this.getSize() - 1) {
                string += ", ";
            }
        }

        string += "]}";

        return string;
    }
}
