package com.sweng.entity;

import java.util.List;

public class Story {
    private String title;
    private String description;
    private List<Scenario> scenarios;

    // Costruttore
    public Story(String title, String description, List<Scenario> scenarios) {
        this.title = title;
        this.description = description;
        this.scenarios = scenarios;
    }

    // Metodi getter e setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }
}

