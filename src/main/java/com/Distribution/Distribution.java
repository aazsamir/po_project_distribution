package com.Distribution;

import com.Distribution.Input.Input;
import com.Distribution.Input.InputCollection;
import com.Distribution.Target.Target;
import com.Distribution.Target.TargetCollection;
import com.Distribution.Utils.DoubleArray;

public class Distribution {
    private TargetCollection targetCollection;
    private InputCollection inputCollection;
    private Calculator calculator;

    public Distribution(TargetCollection targetCollection, InputCollection inputCollection) {
        this.targetCollection = targetCollection;
        this.inputCollection = inputCollection;
        this.calculator = new Calculator(inputCollection, targetCollection);
    }

    public void distribute() {
        Double score;
        Double bestScore;
        Input bestInput;
        Integer bestInputIndex = null;

        while (this.inputCollection.getSize() > 0) {
            for (Target target : this.targetCollection.getTargets()) {
                bestScore = 0.0;
                bestInput = null;
                bestInputIndex = null;

                for (int i = 0; i < this.inputCollection.getSize(); i++) {
                    if (target.getInputCollection().getSize() > this.getMinInputsCount()) {
                        break;
                    }

                    Input input = this.inputCollection.getInput(i);
                    score = this.calculator.calculate(target, input);

                    if (bestScore == null || score >= bestScore) {
                        bestScore = score;
                        bestInput = input;
                        bestInputIndex = i;
                    }
                }

                if (bestInput != null || bestInputIndex != null) {
                    bestInput.setIdTarget(target.getId());
                    target.getInputCollection().addInput(bestInput);
                    this.inputCollection.remove(bestInputIndex);
                }
            }
        }
    }

    private int getMinInputsCount() {
        int min = this.targetCollection.getInputCollection().getSize();

        for (Target target : this.targetCollection.getTargets()) {
            if (target.getInputCollection().getSize() < min) {
                min = target.getInputCollection().getSize();
            }
        }

        return min;
    }

    public Double calcAverageDeviation() {
        Calculator tempCalculator = new Calculator(
                this.inputCollection,
                this.targetCollection);

        Double[] deviations = new Double[this.targetCollection.getSize()];
        int deviationIndex = 0;

        for (Target target : this.targetCollection.getTargets()) {
            deviations[deviationIndex++] = tempCalculator.calculateDeviation(target);
        }

        return DoubleArray.getAverage(deviations);
    }

    public String toString() {
        String string = "{\"targetCollection\": " + this.targetCollection.toString() + ", \"inputCollection\": "
                + this.inputCollection.toString() + "}";

        return string;
    }
}
