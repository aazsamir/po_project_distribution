package com.Distribution;

import com.Distribution.Input.Input;
import com.Distribution.Input.InputCollection;
import com.Distribution.Target.Target;
import com.Distribution.Target.TargetCollection;

public class Calculator {
    private InputCollection inputCollection;
    private TargetCollection targetCollection;

    public Calculator(InputCollection inputCollection, TargetCollection targetCollection) {
        this.inputCollection = inputCollection;
        this.targetCollection = targetCollection;
    }

    public Double calculate(Target target, Input input) {
        Double deviationBefore;
        Double deviationAfter;

        deviationBefore = this.calculateDeviation(target);
        target.getInputCollection().addInput(input);
        deviationAfter = this.calculateDeviation(target);
        target.getInputCollection().pop();

        return Math.abs(deviationBefore - deviationAfter);
    }

    private Double calculateDeviation(Target target) {
        Double deviation;

        deviation = Math.pow(
                Math.abs(
                        this.inputCollection.getNormalizedValue(
                                target.getInputCollection().getAverageIndicator().getValue())
                                -
                                this.inputCollection.getNormalizedValue(
                                        this.targetCollection.getAverageIndicator().getValue())),
                2);

        return deviation;
    }
}
