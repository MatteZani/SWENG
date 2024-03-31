package com.sweng.entity;

import java.util.ArrayList;
import java.util.List;

public class GameSession {

    private User user;
    private Story currentStory;
    private int currentScenario;
    private List<Integer> inventory;
    private int storyId;

    public GameSession(User user, Story story) {
        this.user = user;
        this.currentStory = story;
        this.currentScenario = story.getInitialScenario();
        this.inventory = new ArrayList<>();
        this.storyId = story.getId();
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Story getCurrentStory() {
        return currentStory;
    }

    public void setCurrentStory(Story currentStory) {
        this.currentStory = currentStory;
    }

    public int getCurrentScenario() {
        return currentScenario;
    }

    public void setCurrentScenario(int currentScenario) {
        this.currentScenario = currentScenario;
    }

    public List<Integer> getInventory() {
        return inventory;
    }

    public void addToInventory(int objectId) {
        if (!inventory.contains(objectId)) {
            inventory.add(objectId);
        }
    }

    public void removeFromInventory(int objectId) {
        inventory.remove(objectId);
    }

    public boolean isObjectInInventory(int objectId) {
        return inventory.contains(objectId);
    }

    public void advanceToNextScenario(String scenarioId) {
    }

    public boolean solveRiddle(String riddleId, String userAnswer) {
        return false;
    }

    public int getStoryId() {
        return this.storyId;
    }
}
