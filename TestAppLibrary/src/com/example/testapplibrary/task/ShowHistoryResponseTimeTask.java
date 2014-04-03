package com.example.testapplibrary.task;

import java.math.BigDecimal;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.example.testapplibrary.database.HttpRequstResultProvider;
import com.example.testapplibrary.util.Constains;


public class ShowHistoryResponseTimeTask extends AsyncTask<Boolean, Integer, String> {

private Context ctx;

public ShowHistoryResponseTimeTask(Context ctx) {
	super();
	this.ctx = ctx;
}

@Override
protected String doInBackground(Boolean... params) {
	ContentResolver resolver = ctx.getContentResolver();
	Cursor cursor = resolver.query(HttpRequstResultProvider.RESPONSE_TIME_CONTENT_URI,
					HttpRequstResultProvider.RESPONSE_TIME_PROJECTION, null, null, null);
	double averageResponseTime = 0;
	if (cursor != null && cursor.getCount() > 1) {
		long totalResponseTime = 0;
		while (cursor.moveToNext()) {
			totalResponseTime += cursor.getLong(1);
			Log.d(Constains.TAG_SHOW_HISTORY, "totalResponseTime : " + totalResponseTime);
			averageResponseTime += cursor.getDouble(2);
			Log.d(Constains.TAG_SHOW_HISTORY, "averageResponseTime : " + averageResponseTime);
		}
		
		Log.d(Constains.TAG_SHOW_HISTORY, "averageResponseTime : " + averageResponseTime);
		Log.d(Constains.TAG_SHOW_HISTORY, "cursor.getCount() : " + cursor.getCount());
		averageResponseTime = new BigDecimal(averageResponseTime).divide(new BigDecimal(cursor.getCount()), 2,
						BigDecimal.ROUND_HALF_UP).doubleValue();
		cursor.close();
		
	} else {
		Log.d(Constains.TAG_SHOW_HISTORY, "No request data");
	}
	return "" + averageResponseTime;
}

@Override
protected void onPostExecute(String result) {
	super.onPostExecute(result);
	AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
	builder.setTitle("History Average Response Time");
	builder.setMessage(result);
	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int arg1) {
			dialog.dismiss();
		}
	});
	builder.show();
}

}
