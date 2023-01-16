package com.Distribution.Target;

import com.Distribution.Input.InputCollection;

public class Target {
    private InputCollection inputCollection;

    public Target(InputCollection inputCollection) {
        this.inputCollection = inputCollection;
    }

    public InputCollection getInputCollection() {
        return this.inputCollection;
    }

    public String toString() {
        return "{\"inputCollection\": " + this.getInputCollection().toString() + "}";
    }
}
