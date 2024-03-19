package com.sweng.entity;

import java.util.List;

public class Scenario {

    private String title;
    private String description;

    private List<Scenario> options;

    // Costruttore
    public Scenario(String description,List<Scenario> options) {
        this.description = description;
        this.options = options;
    }

    // Metodi getter e setter
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Scenario> getOptions() {
        return options;
    }

    public void setOptions(List<Scenario> options) {
        this.options = options;
    }

}

