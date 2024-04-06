package com.sweng.entity;

import java.util.List;

public class Scenario {

    private int id;
    private String description;
    private List<Scenario> nextScenarios;
    private int storyId;
    private int necessaryObjectId;
    private int foundObjectId;

    private int riddleId;

    public Scenario(){

    }

    public Scenario(String description){
        this.description = description;
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

    public Scenario(int id, String description, List<Scenario> nextScenarios, int storyId, int necessaryObjectId, int foundObjectId, int riddleId) {
        this.id = id;
        this.description = description;
        this.nextScenarios = nextScenarios;
        this.storyId = storyId;
        this.necessaryObjectId = necessaryObjectId;
        this.foundObjectId = foundObjectId;
        this.riddleId = riddleId;
    }

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

    public int getNecessaryObjectId() {
        return necessaryObjectId;
    }

    public void setNecessaryObjectId(int necessaryObjectId) {
        this.necessaryObjectId = necessaryObjectId;
    }

    public int getFoundObjectId() {
        return foundObjectId;
    }

    public void setFoundObjectId(int foundObjectId) {
        this.foundObjectId = foundObjectId;
    }

    public int getRiddleId() {
        return riddleId;
    }

    public void setRiddleId(int riddleId) {
        this.riddleId = riddleId;
    }
}

