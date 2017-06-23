package com.sdkv2;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


public class LockActivity extends AppCompatActivity {
    private boolean checkingDrawPermission = false;
    public WindowManager winManager;
    public RelativeLayout wrapperView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        try {
            showLockScreen();
        } catch (WindowManager.BadTokenException e) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkDrawOverlayPermission();
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkingDrawPermission && Settings.canDrawOverlays(this)) {
            recreate();
        } else {

        }
    }

    private void showLockScreen() {
        if (!isMyServiceRunning(StartLockscreenService.class)) {
            startService(new Intent(getBaseContext(), StartLockscreenService.class));
        }

        final WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
                PixelFormat.TRANSLUCENT);


        ((KeyguardManager) getSystemService(KEYGUARD_SERVICE)).newKeyguardLock("IN").disableKeyguard();

        getWindow().setAttributes(localLayoutParams);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        this.winManager = ((WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE));

        this.wrapperView = new RelativeLayout(this);

        mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        mAdView.setAdUnitId("ca-app-pub-8634259134319673/5360298746");
        // Create an ad request.
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        // Optionally populate the ad request builder.
        adRequestBuilder.addTestDevice("64784F27D802A346B5A8F7D52D3CD378");
        // Add the AdView to the view hierarchy.


        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                unLockNow();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                unLockNow();
            }

            @Override
            public void onAdLoaded() {
                try {
                    wrapperView.addView(mAdView);
                    wrapperView.setBackgroundColor(Color.BLACK);
                    winManager.addView(wrapperView, localLayoutParams);
                    SharedPreferencesUtil.setShowing(LockActivity.this,"showing",true);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAdLeftApplication() {
                unLockNow();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.e("test","adopended");
            }
        });

        mAdView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                unLockNow();
                return true;
            }
        });
        mAdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unLockNow();
            }
        });
        mAdView.loadAd(adRequestBuilder.build());

    }
    AdView mAdView;
    @TargetApi(Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            checkingDrawPermission = true;
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }


    public void unLockNow(){
        try {
            this.winManager.removeView(this.wrapperView);
            this.wrapperView.removeAllViews();
            SharedPreferencesUtil.setShowing(LockActivity.this,"showing",false);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
