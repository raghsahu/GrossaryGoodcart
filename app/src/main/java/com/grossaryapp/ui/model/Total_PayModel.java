package com.grossaryapp.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Raghvendra Sahu on 12/12/2019.
 */
public class Total_PayModel implements Serializable {
    String gst;
    String shippinig_charge;
    String discount;
    String total_item_price;
    Integer total_payable;

    public Total_PayModel(String gst, String shippinig_charge, String discount, String total_item_price, Integer total_payable) {
        this.gst = gst;
        this.shippinig_charge = shippinig_charge;
        this.discount = discount;
        this.total_item_price = total_item_price;
        this.total_payable = total_payable;
    }


    public Total_PayModel(Parcel in) {
        gst = in.readString();
        shippinig_charge = in.readString();
        discount = in.readString();
        total_item_price = in.readString();
        total_payable = in.readInt();


    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getShippinig_charge() {
        return shippinig_charge;
    }

    public void setShippinig_charge(String shippinig_charge) {
        this.shippinig_charge = shippinig_charge;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotal_item_price() {
        return total_item_price;
    }

    public void setTotal_item_price(String total_item_price) {
        this.total_item_price = total_item_price;
    }

    public Integer getTotal_payable() {
        return total_payable;
    }

    public void setTotal_payable(Integer total_payable) {
        this.total_payable = total_payable;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//
//    }
//
//    public static final Creator<Total_PayModel> CREATOR = new Creator<Total_PayModel>() {
//        @Override
//        public Total_PayModel createFromParcel(Parcel in) {
//            return new Total_PayModel(in);
//        }
//
//        @Override
//        public Total_PayModel[] newArray(int size) {
//            return new Total_PayModel[size];
//        }
//    };

}
