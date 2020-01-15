package com.grossaryapp.ui.model.OrderHistoryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 14/12/2019.
 */
public class OrderHistory {

    @SerializedName("data")
    @Expose
    private List<OrderHistoryData> data = null;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<OrderHistoryData> getData() {
        return data;
    }

    public void setData(List<OrderHistoryData> data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
