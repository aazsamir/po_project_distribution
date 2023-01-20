package com.Distribution;

import java.sql.SQLException;

import com.Distribution.Csv.Reader;
import com.Distribution.Input.Input;
import com.Distribution.Input.InputCollection;
import com.Distribution.Repository.Database;
import com.Distribution.Repository.Faker;
import com.Distribution.Repository.Repository;
import com.Distribution.Target.Target;
import com.Distribution.Target.TargetCollection;

public class App {
    public static void main(String[] args) {
        try {
            // repository init
            Repository repository = null;
            Faker faker = new Faker();

            repository = createRepository();
            repository.migrateIfEmpty();
            // repository.migrate();

            // distribution init
            TargetCollection targetCollection = repository.findTargets();
            // InputCollection inputCollection = faker.mockTestInputCollection(10, null);
            InputCollection inputCollection = InputCollection
                    .makeFromIndicatorCollection(new Reader().readIndicatorsCsv());

            System.out.println(inputCollection.toString());

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

            repository.close();
        } catch (Exception sqlException) {
            System.out.println("FAILED!");
            System.err.println(sqlException.getMessage());
        }
    }

    private static Repository createRepository() throws SQLException {
        return new Repository(new Database());
    }
}
