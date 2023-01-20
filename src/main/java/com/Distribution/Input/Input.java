package com.Distribution.Input;

import com.Distribution.Indicator.Indicator;

public class Input {
    private Integer id;
    private Integer idTarget;
    private Indicator indicator;

    public Input(Indicator indicator, Integer id) {
        this.setIndicator(indicator);
        this.id = id;
        this.idTarget = null;
    }

    public Input(Indicator indicator, Integer id, Integer idTarget) {
        this.setIndicator(indicator);
        this.id = id;
        this.idTarget = idTarget;
    }

    public Indicator getIndicator() {
        return this.indicator;
    }

    public Integer getId() {
        return this.id;
    }

    public Integer getIdTarget() {
        return this.idTarget;
    }

    private void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    public String toString() {
        return "{\"indicator\": " + this.getIndicator().toString() + "}";
    }
}
