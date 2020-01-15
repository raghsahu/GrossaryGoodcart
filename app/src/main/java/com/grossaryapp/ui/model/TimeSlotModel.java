package com.grossaryapp.ui.model;

/**
 * Created by Raghvendra Sahu on 24/12/2019.
 */
public class TimeSlotModel {
    String timeSlot_id;
    String from_time;
    String to_time;

    public TimeSlotModel(String timeSlot_id, String from_time, String to_time) {

        this.timeSlot_id=timeSlot_id;
        this.from_time=from_time;
        this.to_time=to_time;
    }

    public String getTimeSlot_id() {
        return timeSlot_id;
    }

    public void setTimeSlot_id(String timeSlot_id) {
        this.timeSlot_id = timeSlot_id;
    }

    public String getFrom_time() {
        return from_time;
    }

    public void setFrom_time(String from_time) {
        this.from_time = from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }
}
