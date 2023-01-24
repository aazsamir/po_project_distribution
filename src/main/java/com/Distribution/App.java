package com.Distribution;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import com.Distribution.Csv.Reader;
import com.Distribution.Csv.Writer;
import com.Distribution.Input.InputCollection;
import com.Distribution.Repository.Database;
import com.Distribution.Repository.Faker;
import com.Distribution.Repository.Repository;
import com.Distribution.Target.TargetCollection;

public class App {
    private static final boolean FAKER_DATA = false;
    private static final boolean FORCE_MIGRATE = false;
    private static final boolean IS_DEBUG = false;

    private static String inputCsv = "input.csv";
    private static String outputCsv = "output.csv";

    public static void main(String[] args) {
        try {
            Repository repository = initRepository();
            InputCollection inputCollection = getInputCollection();

            TargetCollection targetCollection = repository.findTargets();

            Distribution distribution = new Distribution(
                    targetCollection,
                    inputCollection);

            distribution.distribute();
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

    private static Repository initRepository() throws SQLException {
        Repository repository = new Repository(new Database(IS_DEBUG));

        if (FORCE_MIGRATE) {
            repository.migrate();
        } else {
            repository.migrateIfEmpty();
        }

        return repository;
    }

    private static InputCollection getInputCollection() throws FileNotFoundException {
        InputCollection inputCollection;

        if (FAKER_DATA) {
            inputCollection = new Faker().mockTestInputCollection(100);
        } else {
            inputCollection = InputCollection
                    .makeFromIndicatorCollection(new Reader(inputCsv).readIndicatorsCsv());
        }

        return inputCollection;
    }
}
