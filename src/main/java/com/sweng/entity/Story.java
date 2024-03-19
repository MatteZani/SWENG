package com.sweng.entity;

public class Story {
    private String title;
    private String description;
    private Scenario initialScenario;

    public Story(String title, String description, Scenario initialScenario) {
        this.title = title;
        this.description = description;
        this.initialScenario = initialScenario;
    }

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

    public Scenario getInitialScenario() {
        return initialScenario;
    }

    public void setInitialScenario(Scenario initialScenario) {
        this.initialScenario = initialScenario;
    }
}


