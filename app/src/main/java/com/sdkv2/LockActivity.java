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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


public class LockActivity extends AppCompatActivity {
    private boolean checkingDrawPermission = false;
    public WindowManager winManager;
    public LinearLayout wrapperView;

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

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams(
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



        this.wrapperView = new LinearLayout(this);
        wrapperView.setOrientation(LinearLayout.VERTICAL);
        // Create a banner ad. The ad size and ad unit ID must be set before calling loadAd.
        mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        mAdView.setAdUnitId("ca-app-pub-8634259134319673/5360298746");
        // Create an ad request.
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        // Optionally populate the ad request builder.
        adRequestBuilder.addTestDevice("64784F27D802A346B5A8F7D52D3CD378");
        // Add the AdView to the view hierarchy.

        final Button btn = new Button(this);
        btn.setText("Skip Ads");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unLockNow();
            }
        });
        btn.setVisibility(View.GONE);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
//                super.onAdClosed();
                unLockNow();
            }

            @Override
            public void onAdLoaded() {
//                super.onAdLoaded();
                btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdLeftApplication() {
//                super.onAdLeftApplication();
                unLockNow();
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

        wrapperView.addView(mAdView);
        wrapperView.addView(btn);
        // Start loading the ad.
        mAdView.loadAd(adRequestBuilder.build());
        this.winManager = ((WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE));
        wrapperView.setBackgroundColor(Color.BLACK);
        this.winManager.addView(wrapperView, localLayoutParams);
    }

    private InterstitialAd mInterstitialAd;
    AdView mAdView;


    @TargetApi(Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            checkingDrawPermission = true;
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    public void onUnlock(View view) {
        this.winManager.removeView(this.wrapperView);
        this.wrapperView.removeAllViews();

        finish();
    }

    public void unLockNow(){
        this.winManager.removeView(this.wrapperView);
        this.wrapperView.removeAllViews();
        finish();
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
