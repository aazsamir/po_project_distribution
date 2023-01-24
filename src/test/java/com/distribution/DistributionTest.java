package com.distribution;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.Distribution.Distribution;
import com.Distribution.Input.InputCollection;
import com.Distribution.Repository.Faker;
import com.Distribution.Target.TargetCollection;

public class DistributionTest {
    @Test
    public void testDistribute() {
        TargetCollection targetCollection = getFaker().mockTestTargetCollection(2);
        InputCollection inputCollection = getFaker().mockTestInputCollection(4);

        Distribution distribution = new Distribution(targetCollection, inputCollection);
        distribution.distribute();

        assertTrue(inputCollection.getSize() == 0);
    }

    private Faker getFaker() {
        return new Faker();
    }
}
