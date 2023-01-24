package com.Distribution.Repository;

import com.Distribution.Indicator.Indicator;
import com.Distribution.Input.Input;
import com.Distribution.Input.InputCollection;
import com.Distribution.Target.Target;
import com.Distribution.Target.TargetCollection;

public class Faker {
    public TargetCollection mockTestTargetCollection(int count) {
        Target[] targets = new Target[count];

        for (int i = 0; i < count; i++) {
            targets[i] = new Target(i, "target" + getRandomInt(), mockTestInputCollection(1, i));
        }

        return new TargetCollection(targets);
    }

    public InputCollection mockTestInputCollection(int count) {
        return mockTestInputCollection(count, null);
    }

    public InputCollection mockTestInputCollection(int count, Integer idTarget) {
        Input[] inputs = new Input[count];

        for (int i = 0; i < count; i++) {
            if (idTarget == null) {
                inputs[i] = new Input(mockTestIndicator(), null);
            } else {
                inputs[i] = new Input(mockTestIndicator(), null, idTarget);
            }
        }

        return new InputCollection(inputs);
    }

    public Indicator mockTestIndicator() {
        return new Indicator(
                getRandomDouble(),
                getRandomDouble(),
                getRandomDouble(),
                getRandomDouble(),
                getRandomBoolean());
    }

    private Double getRandomDouble() {
        return Math.pow(Math.random() * 10, 2);
    }

    private int getRandomInt() {
        return (int) (Math.random() * 100);
    }

    private Boolean getRandomBoolean() {
        return Math.random() > 0.5;
    }
}
