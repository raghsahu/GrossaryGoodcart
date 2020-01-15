package com.grossaryapp.ui.model.OrderHistoryModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Raghvendra Sahu on 14/12/2019.
 */
public class OrderHistoryData implements Serializable , Parcelable {
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("user_data")
    @Expose
    private String userData;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("num_of_item")
    @Expose
    private String numOfItem;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("items")
    @Expose
    private List<HistoryItems> items = null;

    @SerializedName("time_slot")
    @Expose
    private TimeSlotData timeSlot;
    @SerializedName("address")
    @Expose
    private AddressData address;


    protected OrderHistoryData(Parcel in) {
        orderId = in.readString();
        userData = in.readString();
        date = in.readString();
        total = in.readString();
        numOfItem = in.readString();
        paymentStatus = in.readString();
        orderStatus = in.readString();
    }

    public static final Creator<OrderHistoryData> CREATOR = new Creator<OrderHistoryData>() {
        @Override
        public OrderHistoryData createFromParcel(Parcel in) {
            return new OrderHistoryData(in);
        }

        @Override
        public OrderHistoryData[] newArray(int size) {
            return new OrderHistoryData[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getNumOfItem() {
        return numOfItem;
    }

    public void setNumOfItem(String numOfItem) {
        this.numOfItem = numOfItem;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<HistoryItems> getItems() {
        return items;
    }

    public void setItems(List<HistoryItems> items) {
        this.items = items;
    }

    public TimeSlotData getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlotData timeSlot) {
        this.timeSlot = timeSlot;
    }

    public AddressData getAddress() {
        return address;
    }

    public void setAddress(AddressData address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeString(userData);
        dest.writeString(date);
        dest.writeString(total);
        dest.writeString(numOfItem);
        dest.writeString(paymentStatus);
        dest.writeString(orderStatus);
    }




}
