package com.Distribution;

import java.sql.SQLException;

import com.Distribution.Csv.Reader;
import com.Distribution.Csv.Writer;
import com.Distribution.Input.Input;
import com.Distribution.Input.InputCollection;
import com.Distribution.Repository.Database;
import com.Distribution.Repository.Repository;
import com.Distribution.Target.Target;
import com.Distribution.Target.TargetCollection;

public class App {
    public static void main(String[] args) {
        try {
            // repository init
            Repository repository = null;

            repository = createRepository();
            repository.migrateIfEmpty();

            // distribution init
            TargetCollection targetCollection = repository.findTargets();
            InputCollection inputCollection = InputCollection
                    .makeFromIndicatorCollection(new Reader().readIndicatorsCsv());

            Distribution distribution = new Distribution(
                    targetCollection,
                    inputCollection);

            System.out.println(distribution.calcAverageDeviation());
            distribution.distribute();
            System.out.println("after distribute:");
            System.out.println(distribution.calcAverageDeviation());

            for (Target target : targetCollection.getTargets()) {
                repository.updateTarget(target);

                for (Input input : target.getInputCollection().getInputs()) {
                    repository.updateInput(input);
                }
            }

            Writer csvWriter = new Writer();
            csvWriter.writeToFile(targetCollection);

            repository.close();
        } catch (Exception exception) {
            System.out.println("FAILED!");
            exception.printStackTrace(System.err);
            System.err.println(exception.getMessage());
        }
    }

    private static Repository createRepository() throws SQLException {
        return new Repository(new Database());
    }
}
