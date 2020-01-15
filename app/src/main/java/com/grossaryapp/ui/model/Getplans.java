package com.grossaryapp.ui.model;

/**
 * Created by Raghvendra Sahu on 02/11/2019.
 */
public class Getplans {
    String id, name,description,image;

    public Getplans(String id, String name,String description,String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image=image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}

