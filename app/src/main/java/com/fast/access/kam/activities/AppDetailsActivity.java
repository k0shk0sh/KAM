package com.fast.access.kam.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fast.access.kam.R;

import butterknife.ButterKnife;

/**
 * Created by Kosh on 8/18/2015. copyrights are reserved
 */
public class AppDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_details);
        ButterKnife.bind(this);
    }
}
