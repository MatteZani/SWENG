package com.sweng.entity;

import java.util.List;

public class Scenario {

    private int id;
    private String description;
    private List<Scenario> nextScenarios;
    private int storyId;

    // Costruttore
    public Scenario(String description, List<Scenario> nextScenarios, int storyId) {

        this.description = description;
        this.nextScenarios = nextScenarios;
        this.storyId = storyId;
    }

    public Scenario(String description, int storyId) {
        this.description = description;
        this.storyId = storyId;
    }

    public Scenario(int id, String description, int storyId) {
        this.id = id;
        this.description = description;
        this.storyId = storyId;
    }

    // Metodi getter e setter
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Scenario> getNextScenarios() {
        return nextScenarios;
    }

    public void setNextScenarios(List<Scenario> nextScenarios) {
        this.nextScenarios = nextScenarios;
    }
    public void addNextScenario(Scenario nextScenario) { //aggiungere nuovo scenario a nextScenarios
        nextScenarios.add(nextScenario);
    }

    public int getStoryId() {
        return storyId;
    }
    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

