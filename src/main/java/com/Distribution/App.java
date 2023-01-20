package com.Distribution;

import java.sql.SQLException;

import com.Distribution.Repository.Database;
import com.Distribution.Repository.Faker;
import com.Distribution.Repository.Repository;
import com.Distribution.Target.TargetCollection;

public class App {
    public static void main(String[] args) {
        try {
            // repository init
            Repository repository = null;
            Faker faker = new Faker();

            repository = createRepository();
            repository.migrate();

            // distribution init
            TargetCollection targetCollection = repository.findTargets();
            Distribution distribution = new Distribution(
                    targetCollection,
                    faker.mockTestInputCollection(100, null));

            System.out.println(distribution.calcAverageDeviation());
            distribution.distribute();
            System.out.println("after distribute:");
            System.out.println(distribution.calcAverageDeviation());

            // System.out.println(distribution.toString());
            repository.close();
        } catch (SQLException sqlException) {
            System.out.println("FAILED!");
            System.err.println(sqlException.getMessage());
        }
    }

    private static Repository createRepository() throws SQLException {
        return new Repository(new Database());
    }
}
