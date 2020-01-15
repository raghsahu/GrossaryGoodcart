package com.grossaryapp.ui.model;

/**
 * Created by Raghvendra Sahu on 06/11/2019.
 */
public class GetRelatedProducts {
    String id,url_slug,product_id,name,mrp,price,quantity,sub_cate_name,cate_name,image;



    public GetRelatedProducts(String id, String url_slug, String product_id, String name,
                              String mrp, String price, String quantity,
                              String sub_cate_name, String cate_name, String image) {



        this.id = id;
        this.url_slug = url_slug;
        this.product_id=product_id;
        this.name = name;
        this.mrp = mrp ;
        this.price=price;
        this.quantity = quantity;
        this.sub_cate_name = sub_cate_name;
        this.cate_name=cate_name;
        this.image = image;


    }

    public void setId(String id) {
        this.id = id;
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

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setSub_cate_name(String sub_cate_name) {
        this.sub_cate_name = sub_cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getMrp() {
        return mrp;
    }
    public String getPrice() {
        return price;
    }
    public String getQuantity() {
        return quantity;
    }
    public String getSub_cate_name() {
        return sub_cate_name;
    }
    public String getCate_name() {
        return cate_name;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }


}


