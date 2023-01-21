package com.Distribution;

import java.sql.SQLException;

import com.Distribution.Csv.Reader;
import com.Distribution.Csv.Writer;
import com.Distribution.Input.InputCollection;
import com.Distribution.Repository.Database;
import com.Distribution.Repository.Faker;
import com.Distribution.Repository.Repository;
import com.Distribution.Target.TargetCollection;

public class App {
    public static void main(String[] args) {
        try {
            // repository init
            Repository repository = createRepository();
            repository.migrateIfEmpty();

            // distribution init
            String inputCsv = "input.csv";
            String outputCsv = "output.csv";

            TargetCollection targetCollection = repository.findTargets();
            // InputCollection inputCollection = InputCollection
                    // .makeFromIndicatorCollection(new Reader(inputCsv).readIndicatorsCsv());
            InputCollection inputCollection = new Faker().mockTestInputCollection(10, null);

            Distribution distribution = new Distribution(
                    targetCollection,
                    inputCollection);

            System.out.println("Before distribute:");
            System.out.println(distribution.calcAverageDeviation());
            distribution.distribute();
            System.out.println("After distribute:");
            System.out.println(distribution.calcAverageDeviation());

            repository.updateTargetCollectionWithInputs(targetCollection);

            Writer csvWriter = new Writer(outputCsv);
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
