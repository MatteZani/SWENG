package com.sweng.entity;

import java.util.List;

public class ScenarioBuilder {
    private String description;
    private List<Scenario> nextScenarios;
    private int storyId;
    private int id;

    private int storyObjectId;

    public ScenarioBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public ScenarioBuilder setNextScenarios(List<Scenario> nextScenarios) {
        this.nextScenarios = nextScenarios;
        return this;
    }

    public ScenarioBuilder setStoryId(int storyId) {
        this.storyId = storyId;
        return this;
    }

    public ScenarioBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public ScenarioBuilder setStoryObject(int storyObjectId) {
        this.storyObjectId = storyObjectId;
        return this;
    }

    public Scenario build() {
        return new Scenario(description, nextScenarios, storyId, storyObjectId);
    }
}