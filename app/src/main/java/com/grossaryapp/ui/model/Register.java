package com.grossaryapp.ui.model;

/**
 * Created by Raghvendra Sahu on 01/11/2019.
 */
public class Register {


    private String name,mobile,email,password;
    public Register(String name, String mobile, String email,String password) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.password=password;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

}
