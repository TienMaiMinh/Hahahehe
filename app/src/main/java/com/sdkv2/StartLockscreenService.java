package com.sdkv2;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


public class StartLockscreenService extends Service {
	private BroadcastReceiver mReceiver = new StartLockscreenReceiver();

	private BroadcastReceiver receiver = new RestartLock();
	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mReceiver, filter);

		IntentFilter filter2 = new IntentFilter();
		filter2.addAction("YouWillNeverKillMe");


		registerReceiver(mReceiver, filter);
		registerReceiver(receiver,filter2);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("test","on destroy");
		sendBroadcast(new Intent("YouWillNeverKillMe"));
		Intent intent = new Intent();
		intent.setAction("aa");
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		super.onTaskRemoved(rootIntent);
		sendBroadcast(new Intent("YouWillNeverKillMe"));
	}

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		if (level <= Service.TRIM_MEMORY_RUNNING_LOW) {
			Log.e("aaa", "onTrimMemory: ");
			sendBroadcast(new Intent("YouWillNeverKillMe"));
		}
	}
}
