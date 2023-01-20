package com.Distribution.Csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.Distribution.Input.Input;
import com.Distribution.Target.Target;
import com.Distribution.Target.TargetCollection;

public class Writer {
    private String filename;

    private static final char SEPARATOR = ';';

    public Writer() {
        this.filename = "output.csv";
    }

    public void writeToFile(TargetCollection targetCollection) throws IOException {
        File csvFile = new File(this.filename);
        csvFile.createNewFile();
        FileWriter csvWriter = new FileWriter(csvFile);

        for (Target target : targetCollection.getTargets()) {
            for (String line : targetToLines(target)) {
                csvWriter.write(line);
            }
        }

        csvWriter.close();
    }

    private String[] targetToLines(Target target) {
        String[] lines = new String[target.getInputCollection().getInputs().length];

        int i = 0;
        for (Input input : target.getInputCollection().getInputs()) {
            lines[i++] = ""
                    + target.getId() + SEPARATOR
                    + target.getName() + SEPARATOR
                    + input.getId() + SEPARATOR
                    + input.getIndicator().getRoughness() + SEPARATOR
                    + input.getIndicator().getTime() + SEPARATOR
                    + input.getIndicator().getCost() + SEPARATOR
                    + input.getIndicator().getPeople() + SEPARATOR
                    + (input.getIndicator().isSpecial() ? "1" : "0")
                    + "\n";
        }

        return lines;
    }
}
