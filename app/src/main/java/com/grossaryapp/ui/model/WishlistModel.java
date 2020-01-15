package com.grossaryapp.ui.model;

/**
 * Created by Raghvendra Sahu on 15/11/2019.
 */
public class WishlistModel {

    String url_slug;
    String name;
    String price;
    String sub_cate_name;
    String cate_name;
    String id;
    String image;


    public WishlistModel(String url_slug, String name, String price, String sub_cate_name,
                         String cate_name, String id, String image) {
        this.url_slug = url_slug;
        this.name = name;
        this.price = price;
        this.sub_cate_name = sub_cate_name;
        this.cate_name = cate_name;
        this.id = id;
        this.image = image;
    }

    public String getUrl_slug() {
        return url_slug;
    }

    public void setUrl_slug(String url_slug) {
        this.url_slug = url_slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSub_cate_name() {
        return sub_cate_name;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
