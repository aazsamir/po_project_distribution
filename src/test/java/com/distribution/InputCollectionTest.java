package com.distribution;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.Distribution.Indicator.Indicator;
import com.Distribution.Indicator.IndicatorCollection;
import com.Distribution.Input.Input;
import com.Distribution.Input.InputCollection;
import com.Distribution.Repository.Faker;

public class InputCollectionTest {
    @Test
    public void testMakeFromIndicatorCollection() {
        Indicator[] indicators = new Indicator[1];
        indicators[0] = getFaker().mockTestIndicator();

        IndicatorCollection indicatorCollection = new IndicatorCollection(
                indicators);

        InputCollection inputCollection = InputCollection.makeFromIndicatorCollection(indicatorCollection);

        assertTrue(inputCollection.getSize() == 1);
    }

    @Test
    public void testMerge() {
        InputCollection inputCollection1 = getFaker().mockTestInputCollection(1);
        InputCollection inputCollection2 = getFaker().mockTestInputCollection(1);
        InputCollection inputCollection = InputCollection.merge(inputCollection1, inputCollection2);

        assertTrue(inputCollection.getSize() == 2);
    }

    @Test
    public void testFilterByTargetId() {
        InputCollection inputCollectionTarget1 = getFaker().mockTestInputCollection(3, 1);
        InputCollection inputCollectionTarget2 = getFaker().mockTestInputCollection(4, 2);
        InputCollection merged = InputCollection.merge(inputCollectionTarget1, inputCollectionTarget2);
        InputCollection filteredInputCollection = merged.filterByTargetId(2);

        assertTrue(filteredInputCollection.getSize() == 4);
    }

    @Test
    public void testGetInputs() {
        InputCollection inputCollection = getFaker().mockTestInputCollection(4);
        Input[] inputs = inputCollection.getInputs();

        assertTrue(inputs.length == 4);
    }

    @Test
    public void testGetInput() {
        InputCollection inputCollection = getFaker().mockTestInputCollection(4);
        Input input = inputCollection.getInput(1);
        Input sameInput = inputCollection.getInputs()[1];

        assertTrue(input != null);
        assertTrue(input == sameInput);
    }

    @Test
    public void testGetAverageIndicator() {
        Indicator[] indicators = new Indicator[2];
        indicators[0] = getFaker().mockTestIndicator();
        indicators[1] = getFaker().mockTestIndicator();
        InputCollection inputCollection = InputCollection
                .makeFromIndicatorCollection(new IndicatorCollection(indicators));
        Indicator averageIndicator = inputCollection.getAverageIndicator();

        assertTrue(averageIndicator != null);
        assertTrue(
                averageIndicator.getCost() == (indicators[0].getCost() + indicators[1].getCost()) / 2);
        assertTrue(averageIndicator.getPeople() == (indicators[0].getPeople() + indicators[1].getPeople()) / 2);
        assertTrue(
                averageIndicator.getRoughness() == (indicators[0].getRoughness() + indicators[1].getRoughness()) / 2);
        assertTrue(averageIndicator.getTime() == (indicators[0].getTime() + indicators[1].getTime()) / 2);
    }

    @Test
    public void testAddInput() {
        InputCollection inputCollection = getFaker().mockTestInputCollection(4);
        Input input = new Input(getFaker().mockTestIndicator());
        inputCollection.addInput(input);

        assertTrue(inputCollection.getSize() == 5);
    }

    @Test
    public void testPop() {
        InputCollection inputCollection = getFaker().mockTestInputCollection(4);
        inputCollection.pop();

        assertTrue(inputCollection.getSize() == 3);
    }

    @Test
    public void testRemove() {
        InputCollection inputCollection = getFaker().mockTestInputCollection(4);
        inputCollection.remove(1);

        assertTrue(inputCollection.getSize() == 3);
    }

    @Test
    public void testGetNormalizedRoughness() {
        Indicator[] indicators = new Indicator[2];
        indicators[0] = new Indicator(0.0, 0.0, 0.0, 0.0, false);
        indicators[1] = new Indicator(100.0, 0.0, 0.0, 0.0, false);

        InputCollection inputCollection = InputCollection
                .makeFromIndicatorCollection(new IndicatorCollection(indicators));

        assertTrue(inputCollection.getNormalizedRoughness(50.0) == 0.5);
    }

    @Test
    public void testGetNormalizedTime() {
        Indicator[] indicators = new Indicator[2];
        indicators[0] = new Indicator(0.0, 0.0, 0.0, 0.0, false);
        indicators[1] = new Indicator(0.0, 100.0, 0.0, 0.0, false);

        InputCollection inputCollection = InputCollection
                .makeFromIndicatorCollection(new IndicatorCollection(indicators));

        assertTrue(inputCollection.getNormalizedTime(50.0) == 0.5);
    }

    @Test
    public void testGetNormalizedCost() {
        Indicator[] indicators = new Indicator[2];
        indicators[0] = new Indicator(0.0, 0.0, 0.0, 0.0, false);
        indicators[1] = new Indicator(0.0, 0.0, 100.0, 0.0, false);

        InputCollection inputCollection = InputCollection
                .makeFromIndicatorCollection(new IndicatorCollection(indicators));

        assertTrue(inputCollection.getNormalizedCost(50.0) == 0.5);
    }

    @Test
    public void testGetNormalizedPeople() {
        Indicator[] indicators = new Indicator[2];
        indicators[0] = new Indicator(0.0, 0.0, 0.0, 0.0, false);
        indicators[1] = new Indicator(0.0, 0.0, 0.0, 100.0, false);

        InputCollection inputCollection = InputCollection
                .makeFromIndicatorCollection(new IndicatorCollection(indicators));

        assertTrue(inputCollection.getNormalizedPeople(50.0) == 0.5);
    }

    @Test
    public void testGetNormalizedSpecial() {
        Indicator[] indicators = new Indicator[3];
        indicators[0] = new Indicator(0.0, 0.0, 0.0, 0.0, false);
        indicators[1] = new Indicator(0.0, 0.0, 0.0, 0.0, true);
        indicators[2] = new Indicator(0.0, 0.0, 0.0, 0.0, true);

        InputCollection inputCollection = InputCollection
                .makeFromIndicatorCollection(new IndicatorCollection(indicators));

        
        assertTrue(inputCollection.getNormalizedSpecial(1.0) == 0.5);
    }

    private Faker getFaker() {
        return new Faker();
    }
}
