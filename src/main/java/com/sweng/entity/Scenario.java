package com.sweng.entity;

import java.util.List;

public class Scenario {

    private String title;
    private String description;
    private List<Scenario> options;
    private int storyId;

    // Costruttore
    public Scenario(String title, String description, List<Scenario> options, int storyId) {
        this.title = title;
        this.description = description;
        this.options = options;
        this.storyId = storyId;
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

    public int getStoryId() {
        return storyId;
    }
    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }
}

