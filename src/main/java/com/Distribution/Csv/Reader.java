package com.Distribution.Csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

import com.Distribution.Indicator.Indicator;
import com.Distribution.Indicator.IndicatorCollection;

public class Reader {
    private String filename;
    private static final char SEPARATOR = ';';

    private static final int ROUGHNESS_INDEX = 0;
    private static final int TIME_INDEX = 1;
    private static final int COST_INDEX = 2;
    private static final int PEOPLE_INDEX = 3;
    private static final int SPECIAL_INDEX = 4;

    public Reader() {
        this.filename = "source.csv";
    }

    public IndicatorCollection readIndicatorsCsv() throws FileNotFoundException {
        File csvFile = new File(this.filename);
        Scanner scanner = new Scanner(csvFile);
        Vector<Indicator> vectorIndicators = new Vector<Indicator>();
        boolean isFirstLine = true;

        while (scanner.hasNextLine()) {
            if (isFirstLine) {
                scanner.nextLine();
                isFirstLine = false;

                continue;
            }

            String[] datas = scanner.nextLine().split(String.valueOf(SEPARATOR));

            vectorIndicators.add(new Indicator(
                    Double.valueOf(datas[ROUGHNESS_INDEX]),
                    Double.valueOf(datas[TIME_INDEX]),
                    Double.valueOf(datas[COST_INDEX]),
                    Double.valueOf(datas[PEOPLE_INDEX]),
                    Integer.valueOf(datas[SPECIAL_INDEX]) == 0 ? false : true));
        }

        scanner.close();

        Indicator[] indicators = new Indicator[vectorIndicators.size()];

        for (int i = 0; i < vectorIndicators.size(); i++) {
            indicators[i] = vectorIndicators.elementAt(i);
        }

        return new IndicatorCollection(indicators);
    }
}