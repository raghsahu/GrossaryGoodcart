package com.grossaryapp.ui.model.OrderHistoryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Raghvendra Sahu on 23/12/2019.
 */
public class AddressData implements Serializable {

    @SerializedName("ship_address")
    @Expose
    private String shipAddress;

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }
}
