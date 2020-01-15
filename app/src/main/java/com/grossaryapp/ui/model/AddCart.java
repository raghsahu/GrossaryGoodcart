package com.grossaryapp.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Raghvendra Sahu on 06/11/2019.
 */
public class AddCart implements Serializable , Parcelable {
    String url_slug,name,price,quantity,id,image,product_ids;

    public AddCart(String url_slug, String name, String price, String quantity, String id, String image, String product_ids) {
        this.url_slug =url_slug;
        this.name = name;
        this.price = price;
        this.quantity=quantity;
        this.id = id;
        this.image = image;
        this.product_ids = product_ids;

    }

    public AddCart(Parcel in) {
        id = in.readString();
        url_slug = in.readString();
        name = in.readString();
        price = in.readString();
        quantity = in.readString();
        image = in.readString();
        product_ids = in.readString();

    }


    public String getProduct_ids() {
        return product_ids;
    }

    public void setProduct_ids(String product_ids) {
        this.product_ids = product_ids;
    }

    public String getUrl_slug() {
        return url_slug;
    }

    public String getName() {
        return name;
    }
    public String getPrice() {
        return price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }
    public String getId() {
        return id;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Creator<AddCart> CREATOR = new Creator<AddCart>() {
        @Override
        public AddCart createFromParcel(Parcel in) {
            return new AddCart(in);
        }

        @Override
        public AddCart[] newArray(int size) {
            return new AddCart[size];
        }
    };


}


