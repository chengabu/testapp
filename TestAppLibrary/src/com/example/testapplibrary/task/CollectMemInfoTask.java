package com.example.testapplibrary.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.example.testapplibrary.custom.handler.UpdateViewTextHandler;
import com.example.testapplibrary.database.HttpRequstResultProvider;
import com.example.testapplibrary.util.CommonUtils;

public class CollectMemInfoTask implements Runnable {

protected Context context;
protected UpdateViewTextHandler handler;
protected boolean withApi;
protected List<ContentValues> contentValues;

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
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	for (int i = 1; i <= recordSize; i++) {
		executeOneAction(recordSize, format, i);
	}

}

private void executeOneAction(int recordSize, SimpleDateFormat format, int i) {

	preBuildMemoryInfo(recordSize, format, i);

	buildMemoryInfo(format, i);

	postBuildMemoryInfo(recordSize, format, i);

	saveToDb(recordSize, i);

	postBuildsaveToDb(recordSize, i);

}

public void postBuildsaveToDb(int recordSize, int i) {
	
	postMessage(recordSize, i);

	postaction();
}

public void postBuildMemoryInfo(int recordSize, SimpleDateFormat format, int i) {
	// Do nothing
}

public void preBuildMemoryInfo(int recordSize, SimpleDateFormat format, int i) {
	// Do nothing
}

public void postaction() {
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		Log.d("Collect memory info", "fail to wait for 20 seconds");
	}
}

public void postMessage(int recordSize, int i) {
	Log.d("CollectMemInfoTask", "postMessage");
	final Message updateMessage = handler.obtainMessage(0, i, recordSize);

	if (handler != null) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				handler.updateTextView(updateMessage);
			}
		});
	}
}

public void saveToDb(int recordSize, int i) {
	Log.d("CollectMemInfoTask", "saveToDb");
	if (contentValues.size() % 50 == 0 || i >= recordSize) {
		try {
			new InsertDbTask(context, contentValues, HttpRequstResultProvider.MEMORY_CONTENT_URI).call();
			contentValues.clear();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("Collect memory info", "fail to save data to db");
		}
	}
}

protected void buildMemoryInfo(SimpleDateFormat format, int i) {
	Log.d("Collect memory info", "The " + i + " time to collect memory info");
	String currentDate = format.format(new Date());

	ContentValues value = CommonUtils.buildMemoryContentValue(context, currentDate, withApi);
	if (value != null) {
		contentValues.add(value);
	}
}

}
