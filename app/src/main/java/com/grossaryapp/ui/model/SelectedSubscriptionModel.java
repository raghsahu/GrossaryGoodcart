package com.grossaryapp.ui.model;

/**
 * Created by Raghvendra Sahu on 24/12/2019.
 */
public class SelectedSubscriptionModel {
    String product_ids;
    String quantity;
    String price;

    public SelectedSubscriptionModel(String product_ids, String quantity, String price) {
        this.product_ids=product_ids;
        this.quantity=quantity;
        this.price=price;

    }

    public String getProduct_ids() {
        return product_ids;
    }

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

    public void setProduct_ids(String product_ids) {
        this.product_ids = product_ids;
    }
}
