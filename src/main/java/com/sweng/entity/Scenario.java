package com.sweng.entity;

public class Scenario {
    private String description;
    //private List<Option> options;

    // Costruttore
    public Scenario(String description){ //,List<Option> options) {
        this.description = description;
        //this.options = options;
    }

    // Metodi getter e setter
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }*/

}

