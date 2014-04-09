package com.example.testapplibrary.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.ContentValues;
import android.content.Context;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.testapplibrary.custom.handler.UpdateViewTextHandler;
import com.example.testapplibrary.database.HttpRequstResultProvider;

public class CollectMemInfoTask implements Runnable {

private Context context;
private UpdateViewTextHandler handler;
private boolean withApi;
private List<ContentValues> contentValues;

public CollectMemInfoTask(Context context) {
	this(context, false);
}

public CollectMemInfoTask(Context context, boolean withApi) {
	this(context, null, withApi);
}

public CollectMemInfoTask(Context context, UpdateViewTextHandler handler) {
	this(context, handler, false);
}

public CollectMemInfoTask(Context context, UpdateViewTextHandler handler, boolean withApi) {
	super();
	this.context = context;
	this.handler = handler;
	this.withApi = withApi;
	contentValues = new ArrayList<ContentValues>();
}

@Override
public void run() {

	int recordSize = 20000;
	for (int i = 1; i <= recordSize; i++) {
		Log.d("Collect memory info", "The " + i + " time to collect memory info");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		String currentDate = format.format(new Date());

		ActivityManager mamager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo systemMemoryInfo = new MemoryInfo();
		mamager.getMemoryInfo(systemMemoryInfo);
		Debug.MemoryInfo meminfo = new Debug.MemoryInfo();
		Debug.getMemoryInfo(meminfo);

		ContentValues value = new ContentValues();
		value.put(HttpRequstResultProvider.COLUMN_WITH_API, withApi);
		value.put(HttpRequstResultProvider.COLUMN_PPS, meminfo.getTotalPss());

		value.put(HttpRequstResultProvider.COLUMN_PRIVATE_DIRTY, meminfo.getTotalPrivateDirty());
//		value.put(HttpRequstResultProvider.COLUMN_PRIVATE_CLEAN, meminfo.getTotalPrivateClean());
		value.put(HttpRequstResultProvider.COLUMN_SHARED_DIRTY, meminfo.getTotalSharedDirty());
//		value.put(HttpRequstResultProvider.COLUMN_SHARED_CLEAN, meminfo.getTotalSharedClean());
		value.put(HttpRequstResultProvider.COLUMN_CREATED_AT, currentDate);
		value.put(HttpRequstResultProvider.COLUMN_UPDATED_AT, currentDate);
		contentValues.add(value);

		if (contentValues.size() % 50 == 0) {
			try {
				new InsertDbTask(context, contentValues, HttpRequstResultProvider.MEMORY_CONTENT_URI).call();
				contentValues.clear();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("Collect memory info", "fail to save data to db");
			}
		}
		
		final Message updateMessage  = handler.obtainMessage(0, i, recordSize);
		
		if (handler != null) {
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					handler.updateTextView(updateMessage);
				}
			});
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Log.d("Collect memory info", "fail to wait for 20 seconds");
		}
	}

}

}
