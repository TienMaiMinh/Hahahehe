package com.sdkv2;

import android.content.Context;
import android.content.Intent;
public class Controller {
    public static void startLock(Context context){
        context.startService(new Intent(context, StartLockscreenService.class));
    }
}
