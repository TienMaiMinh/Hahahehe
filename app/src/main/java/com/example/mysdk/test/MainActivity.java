package com.example.mysdk.test;

import android.app.Activity;
import android.os.Bundle;

import com.sdkv2.Controller;
import com.sdkv2.R;

public class MainActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Controller.startLock(this);
    }
}
