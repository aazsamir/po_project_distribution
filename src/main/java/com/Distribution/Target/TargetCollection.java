package com.Distribution.Target;

import java.util.Vector;

import com.Distribution.Indicator.Indicator;
import com.Distribution.Indicator.IndicatorCollection;
import com.Distribution.Input.Input;
import com.Distribution.Input.InputCollection;

public class TargetCollection {
    private Target[] targets;

    public TargetCollection(Target[] targets) {
        this.setTargets(targets);
    }

    public Target[] getTargets() {
        return this.targets;
    }

    public int getSize() {
        return this.targets.length;
    }

    private void setTargets(Target[] targets) {
        this.targets = targets;
    }

    public Indicator getAverageIndicator() {
        Indicator[] indicators = new Indicator[this.targets.length];

        for (int i = 0; i < this.targets.length; i++) {
            indicators[i] = this.targets[i].getInputCollection().getAverageIndicator();
        }

        return new IndicatorCollection(indicators).getAverageIndicator();
    }

    public InputCollection getInputCollection() {
        Vector<Input> vectorInputs = new Vector<Input>();

        for (Target target : this.targets) {
            for (Input input : target.getInputCollection().getInputs()) {
                vectorInputs.add(input);
            }
        }

        Input[] inputs = new Input[vectorInputs.size()];

        for (int i = 0; i < vectorInputs.size(); i++) {
            inputs[i] = vectorInputs.elementAt(i);
        }

        return new InputCollection(inputs);
    }

    public String toString() {
        String string = "{\"targets\": [";

        for (Target target : this.targets) {
            string += target.toString() + ", ";
        }

        string = string.substring(0, string.length() - 2);
        string += "]}";

        return string;
    }
}
