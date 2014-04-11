package com.example.testapplibrary.task.cpu;

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
import android.os.Process;
import android.util.Log;

import com.example.testapplibrary.database.HttpRequstResultProvider;
import com.example.testapplibrary.task.ExecuteShellTask;
import com.example.testapplibrary.task.InsertDbTask;
import com.example.testapplibrary.util.CommonUtils;

public class CollectCpuTask implements Runnable {

private Context context;
private Handler handler;

	@Override
	public void run() {
		List<ContentValues> contentValues = new ArrayList<ContentValues>(); 
		ExecuteShellTask task = new ExecuteShellTask("ps");
		int pid = Process.myPid();
		
		int recordSize = 20000;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		for (int i = 1; i <= recordSize; i++) {
			Log.d("Collect memory info", "The " + i + " time to collect memory info");
			String currentDate = format.format(new Date());

			ContentValues value = null;
			try {
				value = CommonUtils.parsePsCommand(task.execute(), String.valueOf(pid));
			} catch (Exception e1) {
				Log.d("CollectCpuTask", "Failed to execute command");
			}
			if (value != null) {
				contentValues.add(value);
			}

			if (contentValues.size() % 50 == 0 || i >= recordSize) {
				try {
//					new InsertDbTask(context, contentValues, HttpRequstResultProvider.MEMORY_CONTENT_URI).call();
					contentValues.clear();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.d("Collect CPU info", "fail to save data to db");
				}
			}
			
			final Message updateMessage  = handler.obtainMessage(0, i, recordSize);
			
			if (handler != null) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						
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
