package com.Distribution.Target;

import com.Distribution.Input.InputCollection;

public class Target {
    private Integer id;
    private InputCollection inputCollection;
    private String name;

    public Target(Integer id, String name, InputCollection inputCollection) {
        this.id = id;
        this.name = name;
        this.inputCollection = inputCollection;
    }

    public InputCollection getInputCollection() {
        return this.inputCollection;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return "{\"inputCollection\": " + this.getInputCollection().toString() + "}";
    }
}
