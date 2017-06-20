package com.sdkv2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RestartLock extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context.getApplicationContext(), StartLockscreenService.class));
    }
}
