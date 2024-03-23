package com.sweng.entity;

public class Story {
    private String title;
    private String plot;
    private Scenario initialScenario;
    private String category;
    private String creator;

    public Story(String title, String plot, Scenario initialScenario, String creator, String category) {
        this.title = title;
        this.plot = plot;
        this.initialScenario = initialScenario;
        this.creator = creator;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public Scenario getInitialScenario() {
        return initialScenario;
    }

    public void setInitialScenario(Scenario initialScenario) {
        this.initialScenario = initialScenario;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}


