package com.grossaryapp.ui.model.OrderHistoryModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Raghvendra Sahu on 14/12/2019.
 */
public class HistoryItems implements Serializable , Parcelable {

    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;

    protected HistoryItems(Parcel in) {
        quantity = in.readString();
        price = in.readString();
        productId = in.readString();
        name = in.readString();
        image = in.readString();
        totalPrice = in.readString();
    }

    public static final Creator<HistoryItems> CREATOR = new Creator<HistoryItems>() {
        @Override
        public HistoryItems createFromParcel(Parcel in) {
            return new HistoryItems(in);
        }

        @Override
        public HistoryItems[] newArray(int size) {
            return new HistoryItems[size];
        }
    };

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quantity);
        dest.writeString(price);
        dest.writeString(productId);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(totalPrice);
    }
}
