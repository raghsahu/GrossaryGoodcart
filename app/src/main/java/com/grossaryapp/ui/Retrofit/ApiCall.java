package com.grossaryapp.ui.Retrofit;

import com.grossaryapp.ui.model.OrderHistoryModel.OrderHistory;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Raghvendra Sahu on 14/12/2019.
 */
public interface ApiCall {

    @FormUrlEncoded
    @POST(Base_Url.get_order_history)
    Call<OrderHistory> GetOrderHistory(
            @Field("user_id")  String userId);

//
//    @GET(Base_Url.get_countries)
//    Call<CountryDeailsModel> GetAllCountry();

}
