package com.grossaryapp.ui.model;

/**
 * Created by Raghvendra Sahu on 05/11/2019.
 */
public class GetproductAllCategory {
    String id, url_slug,product_id,name,mrp,price,sub_cate_name,cate_name,image;

    public GetproductAllCategory(String id, String url_slug,String product_id,
                                  String name,String mrp,String price,String sub_cate_name,
                                  String cate_name,String image) {
        this.id = id;
        this.url_slug = url_slug;
        this.product_id =product_id;
        this.name=name;
        this.mrp=mrp;
        this.price=price;
        this.sub_cate_name=sub_cate_name;
        this.cate_name=cate_name;
        this.image=image;
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
    public void setImage(String image) {
        this.image = image;
    }
}



