package com.grossaryapp.ui.activity.Nouse_other;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.grossaryapp.R;
import com.grossaryapp.ui.activity.Category_Product.SingleProductsActivity;

/**
 * Created by Raghvendra Sahu on 23/10/2019.
 */
public class Tab1Activity extends Fragment {
    LinearLayout linearlayoutcategory;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
       View root= inflater.inflate(R.layout.tab1, container, false);
        linearlayoutcategory=root.findViewById(R.id.linearlayoutcategory);
        linearlayoutcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), SingleProductsActivity.class);

           startActivity(intent);

            }
        });
        return root;
    }
}
