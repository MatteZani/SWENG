package com.sweng.entity;

public class Story {
    private String title;
    private String plot;
    private Scenario initialScenario;
    private String category;
    private int creatorId;

    public Story(String title, String plot, Scenario initialScenario, int creatorId, String category) {
        this.title = title;
        this.plot = plot;
        this.initialScenario = initialScenario;
        this.creatorId = creatorId;
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

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }
}


