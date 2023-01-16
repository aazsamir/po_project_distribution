package com.Distribution;

import com.Distribution.Input.Input;
import com.Distribution.Input.InputCollection;
import com.Distribution.Target.Target;
import com.Distribution.Target.TargetCollection;

public class Distribution {
    private TargetCollection targetCollection;
    private InputCollection inputCollection;
    private Calculator calculator;

    // public Distribution(TargetCollection targetCollection, InputCollection
    // inputCollection, Calculator calculator) {
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
                    Input input = this.inputCollection.getInput(i);
                    score = this.calculator.calculate(target, input);

                    if (bestScore == null || score >= bestScore) {
                        bestScore = score;
                        bestInput = input;
                        bestInputIndex = i;
                    }
                }

                if (bestInput != null || bestInputIndex != null) {
                    target.getInputCollection().addInput(bestInput);
                    this.inputCollection.remove(bestInputIndex);
                }
            }
        }
    }

    public String toString() {
        String string = "{\"targetCollection\": " + this.targetCollection.toString() + ", \"inputCollection\": "
                + this.inputCollection.toString() + "}";

        return string;
    }
}
