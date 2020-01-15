package com.grossaryapp.ui.activity.Nouse_other;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.grossaryapp.R;

/**
 * Created by Raghvendra Sahu on 29/10/2019.
 */
public class Offers extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.offers, container, false);
    }
}
