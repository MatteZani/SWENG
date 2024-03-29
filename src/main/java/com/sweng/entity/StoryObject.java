package com.sweng.entity;

public class StoryObject {

    private int id;
    private String name;
    private String description;

    private int storyId;

    public StoryObject(int id, String name, String description, int storyId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.storyId = storyId;
    }

    public StoryObject(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }
}
