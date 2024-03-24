package com.sweng.entity;

public class StoryBuilder {
    private String title;
    private String plot;
    private int initialScenario;
    private String creator;
    private String category;
    private int id;
    private int initialScenario0;

    public StoryBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public StoryBuilder setPlot(String plot) {
        this.plot = plot;
        return this;
    }

    public StoryBuilder setInitialScenario(int initialScenario) {
        this.initialScenario = initialScenario;
        return this;
    }

    public StoryBuilder setCreator(String creator) {
        this.creator = creator;
        return this;
    }

    public StoryBuilder setCategory(String category) {
        this.category = category;
        return this;
    }

    public StoryBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public StoryBuilder setInitialScenario0(int initialScenario0) {
        this.initialScenario0 = initialScenario0;
        return this;
    }

    public Story build() {
        return new Story(title, plot, initialScenario, creator, category);
    }
}