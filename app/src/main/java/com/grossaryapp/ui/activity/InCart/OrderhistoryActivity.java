package com.grossaryapp.ui.activity.InCart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.grossaryapp.R;
import com.grossaryapp.ui.Retrofit.APIClient;
import com.grossaryapp.ui.Retrofit.ApiCall;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.Utils.Connectivity;
import com.grossaryapp.ui.activity.ActivityNavigation;
import com.grossaryapp.ui.adapter.HistoryAdapter;
import com.grossaryapp.ui.model.OrderHistoryModel.OrderHistory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderhistoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    ImageView orderback;
    RecyclerView recycler_history;
    HistoryAdapter historyAdapter;
    Session session;
    SwipeRefreshLayout swipeToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhistory);
        session=new Session(this);

        orderback=findViewById(R.id.orderback);
        recycler_history=findViewById(R.id.recycler_history);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeToRefresh.setOnRefreshListener(OrderhistoryActivity.this);

        orderback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (Connectivity.isConnected(OrderhistoryActivity.this)){
            getOrderhistory();
        }else {
            Toast.makeText(this, "Please check internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void getOrderhistory() {
        final ProgressDialog progressDialog = new ProgressDialog(OrderhistoryActivity.this,R.style.MyGravity);
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

                        if (response.body().getResult().equalsIgnoreCase("true")){
                            // Toast.makeText(context, "Successfull", Toast.LENGTH_SHORT).show();

                            historyAdapter = new HistoryAdapter(OrderhistoryActivity.this, response.body().getData());
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(OrderhistoryActivity.this, RecyclerView.VERTICAL, false);
                            recycler_history.setLayoutManager(mLayoutManger);
                            recycler_history.setAdapter(historyAdapter);
                            historyAdapter.notifyDataSetChanged();
                            swipeToRefresh.setVisibility(View.VISIBLE);
                        }else {
                            swipeToRefresh.setVisibility(View.GONE);
                            Toast.makeText(OrderhistoryActivity.this, ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();

                        }

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
    public void onBackPressed() {
        //super.onBackPressed();

        Intent intent=new Intent(OrderhistoryActivity.this, ActivityNavigation.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRefresh() {
        swipeToRefresh.setRefreshing(false);

        if (Connectivity.isConnected(OrderhistoryActivity.this)) {
            getOrderhistory();
        } else {
            Toast.makeText(OrderhistoryActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }
    }
}
