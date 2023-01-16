package com.Distribution;

import com.Distribution.Indicator.Indicator;
import com.Distribution.Input.Input;
import com.Distribution.Input.InputCollection;
import com.Distribution.Target.Target;
import com.Distribution.Target.TargetCollection;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        Distribution distribution = new Distribution(mockTestTargetCollection(), mockTestInputCollection(100));
        distribution.distribute();

        System.out.println(distribution.toString());
    }

    private static TargetCollection mockTestTargetCollection() {
        final int TARGET_COUNT = 5;

        Target[] targets = new Target[TARGET_COUNT];

        for (int i = 0; i < TARGET_COUNT; i++) {
            targets[i] = new Target(mockTestInputCollection(1));
        }

        return new TargetCollection(targets);
    }

    private static InputCollection mockTestInputCollection(int count) {
        Input[] inputs = new Input[count];

        for (int i = 0; i < count; i++) {
            inputs[i] = new Input(mockTestIndicator());
        }

        return new InputCollection(inputs);
    }

    private static Indicator mockTestIndicator() {
        return new Indicator(getRandomDouble());
    }

    private static Double getRandomDouble() {
        return Math.pow(Math.random() * 10, 2);
    }
}
