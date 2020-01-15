package com.grossaryapp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.grossaryapp.R;

public class ActivityHelpCenter extends AppCompatActivity {
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_back = findViewById(R.id.iv_back);
        setSupportActionBar(toolbar);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
