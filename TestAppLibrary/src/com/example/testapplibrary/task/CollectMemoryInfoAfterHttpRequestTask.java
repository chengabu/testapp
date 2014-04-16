package com.example.testapplibrary.task;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.util.Log;

import com.example.testapplibrary.custom.handler.UpdateViewTextHandler;

public class CollectMemoryInfoAfterHttpRequestTask extends CollectMemInfoTask {

private long postSleepTime;

public CollectMemoryInfoAfterHttpRequestTask(Context context, boolean withApi) {
	super(context, withApi);
}

public CollectMemoryInfoAfterHttpRequestTask(Context context, UpdateViewTextHandler handler, boolean withApi) {
	super(context, handler, withApi);
}

public CollectMemoryInfoAfterHttpRequestTask(Context context, UpdateViewTextHandler handler) {
	super(context, handler);
}

public CollectMemoryInfoAfterHttpRequestTask(Context context) {
	super(context);
}

public long getPostSleepTime() {
	return postSleepTime;
}

public void setPostSleepTime(long postSleepTime) {
	this.postSleepTime = postSleepTime;
}

@Override
public void postBuildsaveToDb(int recordSize, int i) {
	Log.d("CollectMemoryInfoAfterHttpRequestTask", "postBuildsaveToDb");
	handler.setConstainMessage(String.format("collecting memory info...\n finished %d requests.", i));
	super.postMessage(recordSize, i);
	try {
		Thread.sleep(postSleepTime);
	} catch (InterruptedException e) {
		Log.d("CollectMemoryInfoAfterHttpRequestTask", "Failed to sleep " + postSleepTime + " ms");
		e.printStackTrace();
	}
}

@Override
public void preBuildMemoryInfo(int recordSize, SimpleDateFormat format, int i) {
	Log.d("CollectMemoryInfoAfterHttpRequestTask", "preBuildMemoryInfo");
	CommonHttpRequestTask task = new CommonHttpRequestTask("GET", "http://42.120.169.4/");
	try {
		task.call();
	} catch (Exception e) {
		Log.d("CollectMemoryInfoAfterHttpRequestTask", "Failed send http request " + postSleepTime + " ms");
		e.printStackTrace();
	}
}

@Override
public void postBuildMemoryInfo(int recordSize, SimpleDateFormat format, int i) {
	try {
		Thread.sleep(postSleepTime);
	} catch (InterruptedException e) {
		Log.d("CollectMemoryInfoAfterHttpRequestTask", "Failed to sleep " + postSleepTime + " ms(postBuildMemoryInfo)");
		e.printStackTrace();
	}
	super.buildMemoryInfo(format, i);
}

}
