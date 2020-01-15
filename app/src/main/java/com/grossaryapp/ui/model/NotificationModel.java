package com.grossaryapp.ui.model;

/**
 * Created by Raghvendra Sahu on 14/12/2019.
 */
public class NotificationModel {
    String id;
    String message;
    String date_time;

    public NotificationModel(String id, String message, String date_time) {
        this.id = id;
        this.message = message;
        this.date_time = date_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
