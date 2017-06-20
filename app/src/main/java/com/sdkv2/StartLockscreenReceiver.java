package com.sdkv2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Random;


public class StartLockscreenReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Random random = new Random();
		int n  = random.nextInt(10);
		if (n<4){
			Intent lockscreenIntent = new Intent(context, LockActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(lockscreenIntent);
		}
	}
}
