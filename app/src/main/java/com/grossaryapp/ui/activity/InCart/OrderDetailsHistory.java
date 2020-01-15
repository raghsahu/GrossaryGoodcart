package com.grossaryapp.ui.activity.InCart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.grossaryapp.R;
import com.grossaryapp.ui.Retrofit.APIClient;
import com.grossaryapp.ui.Retrofit.ApiCall;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.Utils.Connectivity;
import com.grossaryapp.ui.adapter.HistoryAdapter;
import com.grossaryapp.ui.adapter.HistoryItemsAdapter;
import com.grossaryapp.ui.model.OrderHistoryModel.HistoryItems;
import com.grossaryapp.ui.model.OrderHistoryModel.OrderHistory;
import com.grossaryapp.ui.model.OrderHistoryModel.OrderHistoryData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsHistory extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener{
    ImageView orderback;
    Session session;
    SwipeRefreshLayout swipeToRefresh;
    HistoryItemsAdapter historyItemsAdapter;
    OrderHistoryData orderHistoryData;
    List<HistoryItems> historyItems=new ArrayList<>();
    RecyclerView total_item_recyc;
    TextView order_id,order_date,tv_total_item,tv_total_rs,order_status,
            payment_status,tv_cancel_order,tv_order_time_slot,tv_order_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_history);

        orderback=findViewById(R.id.orderback);
        total_item_recyc=findViewById(R.id.total_item_recyc);
        order_id=findViewById(R.id.order_id);
        order_date=findViewById(R.id.order_date);
        tv_total_item=findViewById(R.id.tv_total_item);
        tv_total_rs=findViewById(R.id.tv_total_rs);
        order_status=findViewById(R.id.order_status);
        payment_status=findViewById(R.id.payment_status);
        tv_order_time_slot=findViewById(R.id.tv_order_time_slot);
        tv_order_address=findViewById(R.id.tv_order_address);
       // swipeToRefresh = findViewById(R.id.swipeToRefresh);

        orderback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        try {
            if (getIntent()!=null){
                orderHistoryData=(OrderHistoryData) getIntent().getSerializableExtra("order_his_details");

                String order_his_address=getIntent().getStringExtra("order_his_address");
                String order_his_timeslot=getIntent().getStringExtra("order_his_timeslot");

                historyItems=getIntent().getParcelableArrayListExtra("order_his_items");

                order_id.setText(orderHistoryData.getOrderId());
               order_date.setText(orderHistoryData.getDate());
               tv_total_item.setText("Total items: "+orderHistoryData.getNumOfItem());
               tv_total_rs.setText("Total Rs. "+orderHistoryData.getTotal());
                order_status.setText(orderHistoryData.getOrderStatus());
               payment_status.setText(orderHistoryData.getPaymentStatus());

                tv_order_address.setText(order_his_address);
               tv_order_time_slot.setText(order_his_timeslot);


                if (historyItems!=null){

                    historyItemsAdapter = new HistoryItemsAdapter(OrderDetailsHistory.this, historyItems);
                    RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(OrderDetailsHistory.this, RecyclerView.VERTICAL, false);
                    total_item_recyc.setLayoutManager(mLayoutManger);
                    total_item_recyc.setAdapter(historyItemsAdapter);
                    historyItemsAdapter.notifyDataSetChanged();

                }



            }

        }catch (Exception e){

            Log.e("error_his", e.toString());

        }


        if (Connectivity.isConnected(OrderDetailsHistory.this)){
           // getOrderDetailshistory();

        }else {
            Toast.makeText(this, "Please check internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void getOrderDetailshistory() {
        final ProgressDialog progressDialog = new ProgressDialog(OrderDetailsHistory.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        ApiCall apiInterface = APIClient.getClient().create(ApiCall.class);

        Call<OrderHistory> call = apiInterface.GetOrderHistory(session.getUserId());

        call.enqueue(new Callback<OrderHistory>() {
            @Override
            public void onResponse(Call<OrderHistory> call, Response<OrderHistory> response) {

                try{
                    if (response!=null){
                        Log.e("order_history",""+response.body().getResult());

//                        if (response.body().getResult().equalsIgnoreCase("true")){
//                            // Toast.makeText(context, "Successfull", Toast.LENGTH_SHORT).show();
//
//                            historyAdapter = new HistoryDetailsAdapter(OrderDetailsHistory.this, response.body().getData());
//                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(OrderDetailsHistory.this, RecyclerView.VERTICAL, false);
//                            recycler_history.setLayoutManager(mLayoutManger);
//                            recycler_history.setAdapter(historyAdapter);
//                            historyAdapter.notifyDataSetChanged();
//                            swipeToRefresh.setVisibility(View.VISIBLE);
//                        }else {
//                            swipeToRefresh.setVisibility(View.GONE);
//                            Toast.makeText(OrderDetailsHistory.this, ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();
//
//                        }

                    }
                }catch (Exception e){
                    Log.e("error_Dr_l", e.getMessage());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<OrderHistory> call, Throwable t) {
                progressDialog.dismiss();
                // Log.e("error_Dr_login",t.getMessage());
                //Toast.makeText(AllCountries.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onRefresh() {
        swipeToRefresh.setRefreshing(false);

        if (Connectivity.isConnected(OrderDetailsHistory.this)) {
            getOrderDetailshistory();
        } else {
            Toast.makeText(OrderDetailsHistory.this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
