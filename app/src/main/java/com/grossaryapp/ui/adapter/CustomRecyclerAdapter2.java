package com.grossaryapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.grossaryapp.R;
import com.grossaryapp.ui.model.Getplans;
import com.grossaryapp.ui.model.Navigationcategory;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Raghvendra Sahu on 02/11/2019.
 */
public class CustomRecyclerAdapter2 extends RecyclerView.Adapter<CustomRecyclerAdapter2.ViewHolder> {
    private Context context;
    private List<Getplans> getplans;
    public CustomRecyclerAdapter2(Context context, List getplans) {
        this.context = context;
        this.getplans = getplans;
    }
    @Override
    public CustomRecyclerAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.getplansitems, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerAdapter2.ViewHolder holder, int position) {
        holder.itemView.setTag(getplans.get(position));

        Getplans pu =getplans.get(position);
        Glide.with(context)
                .load("https://logicalsofttech.com/goodcart/assets/uploaded/plans/" + getplans.get(position).getImage())
                .override(300, 200)
                .into(holder.plans);
    }



    @Override
    public int getItemCount() {
        return getplans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView plans;

        public ViewHolder(View itemView) {
            super(itemView);


            plans=itemView.findViewById(R.id.plans);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   Getplans cpu = (Getplans) view.getTag();

                   // Toast.makeText(view.getContext(), cpu.getId()+" "+cpu.getImage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
