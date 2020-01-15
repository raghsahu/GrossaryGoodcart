package com.grossaryapp.ui.model;

/**
 * Created by Raghvendra Sahu on 01/11/2019.
 */
public class Navigation {
    private String id;
    private String image, main_title, sub_title;

    public Navigation(String id, String image, String main_title, String sub_title) {
        this.id = id;
        this.image = image;
        this.main_title = main_title;
        this.sub_title = sub_title;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getMain_title() {
        return main_title;
    }

    public String getSub_title() {
        return sub_title;
    }
}

