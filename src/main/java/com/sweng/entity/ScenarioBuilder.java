package com.sweng.entity;

import java.util.List;

public class ScenarioBuilder {
    private int id;
    private String description;
    private List<Scenario> nextScenarios;
    private int storyId;

    private int necessaryObjectId;

    private int foundObjectId;

    private int riddleId;

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

    public ScenarioBuilder setNecessaryObjectId(int necessaryObjectId){
        this.necessaryObjectId = necessaryObjectId;
        return this;

    }
    public ScenarioBuilder setFoundObjectId(int foundObjectId){
        this.foundObjectId = foundObjectId;
        return this;
    }

    public ScenarioBuilder setRiddleId(int riddleId){
        this.riddleId = riddleId;
        return this;
    }



    public Scenario build() {
        return new Scenario(id, description, nextScenarios, storyId, necessaryObjectId, foundObjectId, riddleId);
    }


}