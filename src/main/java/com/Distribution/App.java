package com.Distribution;

import java.sql.SQLException;

import com.Distribution.Indicator.Indicator;
import com.Distribution.Input.Input;
import com.Distribution.Input.InputCollection;
import com.Distribution.Repository.Database;
import com.Distribution.Repository.Repository;
import com.Distribution.Target.Target;
import com.Distribution.Target.TargetCollection;

public class App {
    public static void main(String[] args) {        
        Repository repository = null;
        
        try {
            repository = createRepository();
            repository.migrate();
        } catch (SQLException sqlException) {
            System.out.println("migrate failed");
            System.err.println(sqlException.getMessage());
        }
        
        Distribution distribution = new Distribution(
                mockTestTargetCollection(),
                mockTestInputCollection(100));

        System.out.println(distribution.calcAverageDeviation());
        distribution.distribute();
        System.out.println("after distribute:");
        System.out.println(distribution.calcAverageDeviation());

        // System.out.println(distribution.toString());

        try {
            repository.close();
        } catch (SQLException sqlException) {
            System.out.println("close failed");
            System.err.println(sqlException.getMessage());
        }
    }

    private static Repository createRepository() throws SQLException {
        return new Repository(new Database());
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
        return new Indicator(
                getRandomDouble(),
                getRandomDouble(),
                getRandomDouble(),
                getRandomDouble(),
                getRandomBoolean());
    }

    private static Double getRandomDouble() {
        return Math.pow(Math.random() * 10, 2);
    }

    private static Boolean getRandomBoolean() {
        return Math.random() > 0.5;
    }
}
