package com.grossaryapp.ui.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.grossaryapp.R;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.Utils.Connectivity;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.activity.InCart.ConfirmationActivity;
import com.grossaryapp.ui.activity.InCart.PaymentActivity;
import com.grossaryapp.ui.activity.InCart.OrderDetailsHistory;
import com.grossaryapp.ui.model.OrderHistoryModel.OrderHistoryData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.grossaryapp.ui.Retrofit.Base_Url.add_order_rating_reviews;
import static com.grossaryapp.ui.Retrofit.Base_Url.order_cancel;

/**
 * Created by Raghvendra Sahu on 14/12/2019.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    private Context context;
    private List<OrderHistoryData> addCarts;
    Session session;
    HistoryItemsAdapter historyItemsAdapter;


    public HistoryAdapter(Context context, List addCarts) {
        this.context = context;
        this.addCarts = addCarts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        session=new Session(context);
        if (addCarts.size()>0){
            final OrderHistoryData orderHistoryData =addCarts.get(position);
            holder.order_id.setText(orderHistoryData.getOrderId());
            holder.order_date.setText(orderHistoryData.getDate());
            holder.tv_total_item.setText("Total items: "+orderHistoryData.getNumOfItem());
            holder.tv_total_rs.setText("Total Rs. "+orderHistoryData.getTotal());
            holder.order_status.setText(orderHistoryData.getOrderStatus());
            holder.payment_status.setText(orderHistoryData.getPaymentStatus());

//            if (!orderHistoryData.getPaymentStatus().equalsIgnoreCase("Unpaid")){
//                holder.btn_confirm.setText("View");
//                holder.tv_cancel_order.setVisibility(View.GONE);
//            }else {
//                holder.btn_confirm.setText("Pay now");
//            }

            if (orderHistoryData.getOrderStatus().equalsIgnoreCase("Cancelled")){
                holder.btn_confirm.setText("View");
                holder.tv_cancel_order.setVisibility(View.GONE);
            }else {
                if (orderHistoryData.getPaymentStatus().equalsIgnoreCase("Paid")){
                    holder.tv_cancel_order.setVisibility(View.GONE);
                    holder.btn_confirm.setText("View");
                }else {
                    holder.tv_cancel_order.setVisibility(View.VISIBLE);
                    holder.btn_confirm.setText("Pay now");
                }

            }


            if (addCarts.get(position).getItems()!=null){

                historyItemsAdapter = new HistoryItemsAdapter(context, addCarts.get(position).getItems());
                RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                holder.total_item_recyc.setLayoutManager(mLayoutManger);
                holder.total_item_recyc.setAdapter(historyItemsAdapter);
                historyItemsAdapter.notifyDataSetChanged();

            }

            holder.btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (orderHistoryData.getPaymentStatus().equalsIgnoreCase("Unpaid")
                        && !orderHistoryData.getOrderStatus().equalsIgnoreCase("Cancelled")){
                        String order_id=orderHistoryData.getOrderId();

                        //**go to pay charge
                        Intent intent=new Intent(context, PaymentActivity.class);
                        intent.putExtra("order_id", order_id);
                        intent.putExtra("Total_amount", orderHistoryData.getTotal());
                        context.startActivity(intent);

                    }else {
                        String order_tomeslot=addCarts.get(position).getTimeSlot().getFromTime()+" to "+addCarts.get(position).getTimeSlot().getToTime();
                        //**go to order details
                        Intent intent=new Intent(context, OrderDetailsHistory.class);
                        intent.putExtra("order_his_details", (Serializable) addCarts.get(position));
                        intent.putExtra("order_his_address", addCarts.get(position).getAddress().getShipAddress());
                        intent.putExtra("order_his_timeslot", order_tomeslot);
                       intent.putParcelableArrayListExtra("order_his_items", (ArrayList<? extends Parcelable>) addCarts.get(position).getItems());
                        context.startActivity(intent);
                    }

                }
            });

            holder.tv_cancel_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (orderHistoryData.getPaymentStatus().equalsIgnoreCase("Unpaid")){
                        String order_id=orderHistoryData.getOrderId();
                        OpencancelDialog(order_id);

                    }
                }
            });

        }

    }

    private void OpencancelDialog(final String order_id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context).setTitle("Grossary")
                .setMessage("Are you sure, you want to cancel this order");

        dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                if (Connectivity.isConnected(context)){
                    CancelOrderApi(order_id);
                }else {
                    Toast.makeText(context, "Please check internet", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();

            }


        });
        final AlertDialog alert = dialog.create();
        alert.show();
    }

    private void CancelOrderApi(final String order_id) {
        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, order_cancel,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("cancel_order_res",response.toString());
                        try {
                            progressDialog.dismiss();
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");

                            if (result.equalsIgnoreCase("true")){
                                Toast.makeText(context,""+msg,Toast.LENGTH_LONG).show();

                            }else{
                                Toast.makeText(context,""+msg,Toast.LENGTH_LONG).show();

                            }

                            notifyDataSetChanged();

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", order_id);
                //params.put("user_id", session.getUserId());
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }

    @Override
    public int getItemCount() {
        return addCarts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_id,order_date,tv_total_item,tv_total_rs,order_status,payment_status,tv_cancel_order;
        Button btn_confirm;
        RecyclerView total_item_recyc;

        public ViewHolder(View itemView) {
            super(itemView);
            order_id=itemView.findViewById(R.id.order_id);
            order_date=itemView.findViewById(R.id.order_date);
            tv_total_item=itemView.findViewById(R.id.tv_total_item);
            tv_total_rs=itemView.findViewById(R.id.tv_total_rs);
            order_status=itemView.findViewById(R.id.order_status);
            payment_status=itemView.findViewById(R.id.payment_status);
            btn_confirm=itemView.findViewById(R.id.btn_confirm);
            total_item_recyc=itemView.findViewById(R.id.total_item_recyc);
            tv_cancel_order=itemView.findViewById(R.id.tv_cancel_order);


        }
    }



}
