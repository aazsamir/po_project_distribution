package com.Distribution;

import com.Distribution.Indicator.Indicator;
import com.Distribution.Input.Input;
import com.Distribution.Input.InputCollection;
import com.Distribution.Target.Target;
import com.Distribution.Target.TargetCollection;
import com.Distribution.Utils.DoubleArray;

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

    public Double calculateDeviation(Target target) {
        Double[] deviations = new Double[Indicator.FIELDS_COUNT - 1]; // @tood -1 is hack

        deviations[0] = Math.pow(
                Math.abs(
                        this.inputCollection.getNormalizedRoughness(
                                target.getInputCollection().getAverageIndicator().getRoughness())
                                -
                                this.inputCollection.getNormalizedRoughness(
                                        this.targetCollection.getAverageIndicator().getRoughness())),
                2);

        deviations[1] = Math.pow(
                Math.abs(
                        this.inputCollection.getNormalizedCost(
                                target.getInputCollection().getAverageIndicator().getCost())
                                -
                                this.inputCollection.getNormalizedCost(
                                        this.targetCollection.getAverageIndicator().getCost())),
                2);

        deviations[2] = Math.pow(
                Math.abs(
                        this.inputCollection.getNormalizedPeople(
                                target.getInputCollection().getAverageIndicator().getPeople())
                                -
                                this.inputCollection.getNormalizedPeople(
                                        this.targetCollection.getAverageIndicator().getPeople())),
                2);

        deviations[3] = Math.pow(
                Math.abs(
                        this.inputCollection.getNormalizedTime(
                                target.getInputCollection().getAverageIndicator().getTime())
                                -
                                this.inputCollection.getNormalizedTime(
                                        this.targetCollection.getAverageIndicator().getTime())),
                2);

        return DoubleArray.getSum(deviations);
    }
}
