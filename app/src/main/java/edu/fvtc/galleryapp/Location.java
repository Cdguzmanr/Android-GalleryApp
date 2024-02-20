package edu.fvtc.galleryapp;

public class Location {
    private String name;
    private String description;

    // Constructor
    public Location(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }


}

