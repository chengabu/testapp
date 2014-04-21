package com.example.testapplibrary.services;

import java.util.HashMap;
import java.util.Map;

import android.app.IntentService;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Process;
import android.util.Log;

public class CommonStartedService extends IntentService {
	
	public static final String TAG = "CommonStartedService";
	private Map<String, Integer> startServiceMaps = new HashMap<String, Integer>();

	public CommonStartedService() {
		super(TAG);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String uuid = intent.getStringExtra("UUID");
		
		if (uuid != null) {
			if (startServiceMaps.containsKey(uuid)) {
				Log.d(TAG, "onStartCommand stop uuid " +uuid);
				stopSelf(startServiceMaps.get(uuid));
				return START_REDELIVER_INTENT;
			}
			startServiceMaps.put(uuid, startId);
			for (String key : startServiceMaps.keySet()) {
				Log.d(TAG, "uuid added "+ key);
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	public CommonStartedService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		int uid = Process.myUid();
		for (int i = 0; i < 10000; i++ ) {
			Log.d(TAG, "CommonStartedService onHandleIntent : " + i);
			logTrafficConsume(uid);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.d(TAG, "CommonStartedService onHandleIntent InterruptedException");
				e.printStackTrace();
			}
		}
	}

	public void logTrafficConsume(int uid) {
		long txByte = TrafficStats.getUidTxBytes(uid);
		long rxByte = TrafficStats.getUidRxBytes(uid);
		long txPacket = TrafficStats.getUidTxPackets(uid);
		long rxPacket = TrafficStats.getUidRxPackets(uid);
		long txByteMobile = TrafficStats.getMobileTxBytes();
		long rxByteMobile = TrafficStats.getMobileRxBytes();
		long txPacketMobile = TrafficStats.getMobileRxPackets();
		long rxPacketMobile = TrafficStats.getMobileRxPackets();
		Log.d(TAG, "uid : " + uid);
		Log.d(TAG, "txByte : " + txByte);
		Log.d(TAG, "rxByte : " + rxByte);
		Log.d(TAG, "txPacket : " + txPacket);
		Log.d(TAG, "rxPacket : " + rxPacket);
		Log.d(TAG, "txByteMobile : " + txByteMobile);
		Log.d(TAG, "rxByteMobile : " + rxByteMobile);
		Log.d(TAG, "txPacketMobile : " + txPacketMobile);
		Log.d(TAG, "rxPacketMobile : " + rxPacketMobile);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "CommonStartedService onDestroy");
	}

}
