package com.grossaryapp.ui.model;

/**
 * Created by Raghvendra Sahu on 14/11/2019.
 */
public class CityData {

    String id;
    String name;

    public CityData(String id, String name) {
        this.id=id;
        this.name=name;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
}
