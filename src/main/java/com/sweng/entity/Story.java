package com.sweng.entity;

public class Story {

    public int id;
    private String title;
    private String plot;
    private int initialScenario;
    private String category;
    private String creator;

    public Story() {
    }

    public Story(String title, String plot, int initialScenario, String creator, String category) {
        this.title = title;
        this.plot = plot;
        this.initialScenario = initialScenario;
        this.creator = creator;
        this.category = category;
    }

    public Story(int id, String title, String plot, int initialScenario, String category, String creator) {
        this.id = id;
        this.title = title;
        this.plot = plot;
        this.initialScenario = initialScenario;
        this.category = category;
        this.creator = creator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getInitialScenario() {
        return initialScenario;
    }

    public void setInitialScenario(int initialScenario) {
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