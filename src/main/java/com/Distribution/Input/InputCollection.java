package com.Distribution.Input;

import java.util.Vector;

import com.Distribution.Indicator.Indicator;
import com.Distribution.Indicator.IndicatorCollection;

public class InputCollection {
    private Vector<Input> inputs;

    public InputCollection(Input[] inputs) {
        this.setInputs(inputs);
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
            this.inputs.add(inputs[i]);
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

    public Double getNormalizedValue(Double value) {
        Double min = null;
        Double max = null;

        for (Input input : this.inputs) {
            if (min == null || input.getIndicator().getValue() < min) {
                min = input.getIndicator().getValue();
            }

            if (max == null || input.getIndicator().getValue() > max) {
                max = input.getIndicator().getValue();
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
