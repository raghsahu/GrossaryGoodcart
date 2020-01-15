package com.grossaryapp.ui.model;

/**
 * Created by Raghvendra Sahu on 01/11/2019.
 */
public class Navigationcategory {
    String id, name,image;

    public Navigationcategory(String id, String name,String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
