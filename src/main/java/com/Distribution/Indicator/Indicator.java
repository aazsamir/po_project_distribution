package com.Distribution.Indicator;

import com.Distribution.Utils.Clamp;

public class Indicator {
    private final static Double MIN_ROUGHNESS = 0.0;
    private final static Double MAX_ROUGHNESS = 1.0;

    private final static Double MIN_TIME = 0.0;
    private final static Double MAX_TIME = 1.0;

    private final static Double MIN_COST = 0.0;
    private final static Double MAX_COST = 1.0;

    private final static Double MIN_PEOPLE = 0.0;
    private final static Double MAX_PEOPLE = 1.0;

    public final static int FIELDS_COUNT = 5;

    private Double roughness;
    private Double time;
    private Double cost;
    private Double people;
    private Boolean special;

    public Indicator() {
        this.setRoughness(0.0);
        this.setTime(0.0);
        this.setCost(0.0);
        this.setPeople(0.0);
        this.setSpecial(false);
    }

    public Indicator(Double roughness, Double time, Double cost, Double people, Boolean isSpecial) {
        this.setRoughness(roughness);
        this.setTime(time);
        this.setCost(cost);
        this.setPeople(people);
        this.setSpecial(isSpecial);
    }

    public Double getRoughness() {
        return this.roughness;
    }

    public void setRoughness(Double roughness) {
        // roughness = Clamp.clamp(roughness, MIN_ROUGHNESS, MAX_ROUGHNESS);

        this.roughness = roughness;
    }

    public Double getTime() {
        return this.time;
    }

    public void setTime(Double time) {
        // time = Clamp.clamp(time, MIN_TIME, MAX_TIME);

        this.time = time;
    }

    public Double getCost() {
        return this.cost;
    }

    public void setCost(Double cost) {
        // cost = Clamp.clamp(cost, MIN_COST, MAX_COST);

        this.cost = cost;
    }

    public Double getPeople() {
        return this.people;
    }

    public void setPeople(Double people) {
        // people = Clamp.clamp(people, MIN_PEOPLE, MAX_PEOPLE);

        this.people = people;
    }

    public Boolean isSpecial() {
        return this.special;
    }

    public void setSpecial(Boolean special) {
        this.special = special;
    }

    public String toString() {
        return "{\"roughness\": " + this.getRoughness() + ", \"time\": " + this.getTime() + ", \"cost\": " + this.getCost()
                + ", \"people\": " + this.getPeople() + ", \"isSpecial\": " + this.isSpecial() + "}";
    }
}
