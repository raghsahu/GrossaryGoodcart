package com.grossaryapp.ui.model;

/**
 * Created by Raghvendra Sahu on 18/11/2019.
 */
public class AddressModel {

    String id;
    String user_id;
    String first_name;
    String last_name;
    String email;
    String mobile;
    String address;
    String address2;
    String city_id;
    String area_id;
    String google_location;
    String lat;
    String lng;
    String zipcode;
    String country;
    String state;
    String address_image;
    String area_name;
    String city_name;

    public AddressModel(String id, String user_id, String first_name, String last_name, String email, String mobile, String address, String address2, String city_id, String area_id, String google_location, String lat, String lng, String zipcode, String country, String state, String address_image, String area_name, String city_name) {
        this.id = id;
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.address2 = address2;
        this.city_id = city_id;
        this.area_id = area_id;
        this.google_location = google_location;
        this.lat = lat;
        this.lng = lng;
        this.zipcode = zipcode;
        this.country = country;
        this.state = state;
        this.address_image = address_image;
        this.area_name = area_name;
        this.city_name = city_name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getGoogle_location() {
        return google_location;
    }

    public void setGoogle_location(String google_location) {
        this.google_location = google_location;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress_image() {
        return address_image;
    }

    public void setAddress_image(String address_image) {
        this.address_image = address_image;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}
