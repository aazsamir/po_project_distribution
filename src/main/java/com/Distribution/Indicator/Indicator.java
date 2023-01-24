package com.Distribution.Indicator;

public class Indicator {
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
        this.roughness = roughness;
    }

    public Double getTime() {
        return this.time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Double getCost() {
        return this.cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getPeople() {
        return this.people;
    }

    public void setPeople(Double people) {
        this.people = people;
    }

    public Boolean isSpecial() {
        return this.special;
    }

    public void setSpecial(Boolean special) {
        this.special = special;
    }

    public String toString() {
        return "{\"roughness\": " + this.getRoughness() + ", \"time\": " + this.getTime() + ", \"cost\": "
                + this.getCost()
                + ", \"people\": " + this.getPeople() + ", \"isSpecial\": " + this.isSpecial() + "}";
    }
}
