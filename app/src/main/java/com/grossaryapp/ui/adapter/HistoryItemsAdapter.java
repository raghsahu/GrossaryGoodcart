package com.grossaryapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grossaryapp.R;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.model.OrderHistoryModel.HistoryItems;
import com.grossaryapp.ui.model.OrderHistoryModel.OrderHistoryData;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 14/12/2019.
 */
public class HistoryItemsAdapter  extends RecyclerView.Adapter<HistoryItemsAdapter.ViewHolder>{
    private Context context;
    private List<HistoryItems> addCarts;
    Session session;



    public HistoryItemsAdapter(Context context, List addCarts) {
        this.context = context;
        this.addCarts = addCarts;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_food_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        session=new Session(context);
        if (addCarts.size()>0){
            HistoryItems orderHistoryData =addCarts.get(position);
            holder.tv_item_name.setText(orderHistoryData.getName());
            holder.tv_quantity.setText(orderHistoryData.getQuantity());
            holder.tv_price.setText(orderHistoryData.getPrice());
            holder.tv_total.setText(orderHistoryData.getTotalPrice());


        }



    }

    @Override
    public int getItemCount() {
        return addCarts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item_name,tv_quantity,tv_price,tv_total;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_item_name=itemView.findViewById(R.id.tv_item_name);
            tv_quantity=itemView.findViewById(R.id.tv_quantity);
            tv_price=itemView.findViewById(R.id.tv_price);
            tv_total=itemView.findViewById(R.id.tv_total);


        }
    }
}
