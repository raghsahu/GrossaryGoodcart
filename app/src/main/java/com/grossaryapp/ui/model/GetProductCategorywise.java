package com.grossaryapp.ui.model;

/**
 * Created by Raghvendra Sahu on 02/11/2019.
 */
public class GetProductCategorywise {
    String id, url_slug,product_id,name,mrp,price,sub_cate_name,cate_name,image,cart_status,cart_quantity;

    public GetProductCategorywise(String id, String url_slug, String product_id,
                                  String name, String mrp, String price, String sub_cate_name,
                                  String cate_name, String image, String cart_status, String cart_quantity) {
        this.id = id;
        this.url_slug = url_slug;
        this.product_id =product_id;
        this.name=name;
        this.mrp=mrp;
        this.price=price;
        this.sub_cate_name=sub_cate_name;
        this.cate_name=cate_name;
        this.image=image;
        this.cart_status=cart_status;
        this.cart_quantity=cart_quantity;
    }

    public String getId() {
        return id;
    }

    public String getUrl_slug() {
        return url_slug;
    }
    public String getProduct_id() {
        return product_id;
    }
    public String getName() {
        return name;
    }
    public String getMrp() {
        return mrp;
    }
    public String getPrice() {
        return price;
    }
    public String getSub_cate_name() {
        return sub_cate_name;
    }
    public String getImage() {
        return image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCart_quantity() {
        return cart_quantity;
    }

    public void setCart_quantity(String cart_quantity) {
        this.cart_quantity = cart_quantity;
    }

    public void setUrl_slug(String url_slug) {
        this.url_slug = url_slug;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSub_cate_name(String sub_cate_name) {
        this.sub_cate_name = sub_cate_name;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getCart_status() {
        return cart_status;
    }

    public void setCart_status(String cart_status) {
        this.cart_status = cart_status;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

