package com.distribution;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import com.Distribution.Repository.Database;
import com.Distribution.Repository.Faker;
import com.Distribution.Repository.Repository;
import com.Distribution.Target.TargetCollection;

public class RepositoryTest {
    @Test
    public void testUpdateTargetCollectionWithInputs() {
        Repository repository = getRepository();

        try {
            repository.updateTargetCollectionWithInputs(
                getFaker().mockTestTargetCollection(2)
            );
            assertTrue(true);
            repository.close();
        } catch (SQLException exception) {
            assertTrue(exception.getMessage(), false);
        }
    }

    @Test
    public void testFindTargets() {
        Repository repository = getRepository();

        try {
            TargetCollection targetCollection = repository.findTargets();
            assertTrue(targetCollection.getSize() > 0);
            repository.close();
        } catch (SQLException exception) {
            assertTrue(exception.getMessage(), false);
        }
    }

    @Test
    public void testFindInputsByIdTarget() {
        Repository repository = getRepository();

        try {
            TargetCollection targetCollection = repository.findTargets();
            assertTrue(targetCollection.getSize() > 0);
            assertTrue(repository.findInputsByIdTarget(targetCollection.getTargets()[0].getId()).getSize() > 0);
            repository.close();
        } catch (SQLException exception) {
            assertTrue(exception.getMessage(), false);
        }
    }

    private Repository getRepository() {
        try {
            Repository repository = new Repository(new Database("testdb.db"));
            repository.migrate();

            return repository;
        } catch (SQLException exception) {
            assertTrue(exception.getMessage(), false);

            return null;
        }
    }

    private Faker getFaker() {
        return new Faker();
    }
}
