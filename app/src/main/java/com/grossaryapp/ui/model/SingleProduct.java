package com.grossaryapp.ui.model;

/**
 * Created by Raghvendra Sahu on 05/11/2019.
 */
public class SingleProduct {
    String id,product_id,image;


    public SingleProduct(String id, String product_id,String image) {



        this.id = id;
        this.product_id = product_id;
        this.image=image;

    }

    public String getId() {
        return id;
    }


    public String getProduct_id() {
        return product_id;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}





