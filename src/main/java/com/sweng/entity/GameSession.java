package com.sweng.entity;

public class GameSession {

    private User user;
    private Story currentStory;
    private int currentScenario;

    public GameSession() {
    }
    public GameSession(Story story, User user, Integer currentScenario) {
        this.user = user;
        this.currentStory = story;
        this.currentScenario = currentScenario;
    }

    public User getUser() {
        return user;
    }

    public void setUsername(User user) {
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

}
