package com.example.testapplibrary.task;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.testapplibrary.model.HttpRequestModel;

public class HttpRequestAsyncTask extends AsyncTask<List<String>, Integer, List<String>> {

private static final String TAG_ON_POST_EXECUTE = "HttpRequestAsyncTask.onPostExecute";

private static final String RESULT_TEMPLE_WITH_CONTENT = "%s - %s \n response time : %s \n response content : %s";
private static final String RESULT_TEMPLE = "%s - %s \n response time : %s \n";

private ArrayAdapter<String> adapter;

List<HttpRequestModel> models = new ArrayList<HttpRequestModel>();

private boolean saveToDb;

private boolean saveResponseContent;

private Context ctx;

private boolean https;

public HttpRequestAsyncTask(Context ctx, ArrayAdapter<String> adapter, boolean saveToDb,
							boolean saveResponseContent, boolean https) {
	super();
	this.adapter = adapter;
	this.saveToDb = saveToDb;
	this.saveResponseContent = saveResponseContent;
	this.ctx = ctx;
	this.https = https;
}

public HttpRequestAsyncTask(Context ctx, ArrayAdapter<String> adapter, boolean saveToDb, boolean saveResponseContent) {
	this(ctx, adapter, saveToDb, saveResponseContent, false);
}

public HttpRequestAsyncTask(Context ctx, ArrayAdapter<String> adapter, boolean saveToDb) {
	this(ctx, adapter, saveToDb, false, false);
}

public HttpRequestAsyncTask(Context ctx, ArrayAdapter<String> adapter) {
	this(ctx, adapter, false, false, false);
}

@Override
protected List<String> doInBackground(List<String>... params) {
	List<String> result = new ArrayList<String>();
	ExecutorService service = Executors.newFixedThreadPool(5);
	List<Future<HttpRequestModel>> futures = new ArrayList<Future<HttpRequestModel>>();
	List<String> urls;
	try {
		if (params != null) {
			models.clear();
			urls = params[0];
			for (String url : urls) {
				futures.add(service.submit(new CommonHttpRequestTask("GET", url)));
			}
			for (Future<HttpRequestModel> future : futures) {
				try {
					HttpRequestModel model = future.get();
					models.add(model);
					result.add(String.format(RESULT_TEMPLE, model.getRequestUrl(),
									String.valueOf(model.getResponseCode()), String.valueOf(model.getResponseTime())));
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
	} finally {
		if (service != null) {
			service.shutdownNow();
		}
	}

	return result;
}

@Override
protected void onPostExecute(List<String> results) {
	super.onPostExecute(results);
	Log.d(TAG_ON_POST_EXECUTE, "Finish super onPostExecute");

	for (String result : results) {
		adapter.add(result);
	}
	adapter.notifyDataSetChanged();
	Log.d(TAG_ON_POST_EXECUTE, "Finish notifyDataSetChanged");

	showAverageResponseTime();

	new Thread(new SaveToDbTask()).start();
}

public void showAverageResponseTime() {
	long totalResponseTime = 0;
	for (HttpRequestModel model : models) {
		totalResponseTime += model.getResponseTime();
	}

	String averageResponseTimeStr = "";
	AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
	builder.setTitle("Average Response Time");
	if (models.size() > 0) {
		averageResponseTimeStr = new BigDecimal(totalResponseTime).divide(new BigDecimal(models.size()), 2,
						BigDecimal.ROUND_HALF_UP).toString();
		builder.setMessage("" + averageResponseTimeStr + "(ms)");
	} else {
		builder.setMessage("No requests are made.");
	}
	AlertDialog dialog = builder.create();
	dialog.show();
}

class SaveToDbTask implements Runnable {
@Override
public void run() {
	if (saveToDb) {
		ContentResolver resolver = ctx.getContentResolver();

		ContentValues responseTimeValues = new ContentValues();
		String uuid = UUID.randomUUID().toString();
		String currentDate = "";
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:MM:ss.SSS");
			currentDate = format.format(new Date());
		} catch (Exception e) {
			Log.e("SaveToDbTask", "Failed to format date", e);
		}
		
		insertResponseTimeRecord(resolver, responseTimeValues, uuid, currentDate);

		insertHttpRecord(resolver, uuid, currentDate);
		Log.d(TAG_ON_POST_EXECUTE, "Finish save data to db");
	}
}

private void insertResponseTimeRecord(ContentResolver resolver, ContentValues responseTimeValues, String uuid,
				String currentDate) {
	long totalResponseTime = 0;
	double averageResponseTime = 0;

	for (HttpRequestModel model : models) {
		totalResponseTime += model.getResponseTime();
	}

	if (models.size() > 0) {
		averageResponseTime = new BigDecimal(totalResponseTime).divide(new BigDecimal(models.size()), 2,
						BigDecimal.ROUND_HALF_UP).doubleValue();
	}

//	responseTimeValues.put(HttpRequstResultProvider.COLUMN_ID, uuid);
//	responseTimeValues.put(HttpRequstResultProvider.COLUMN_TOTAL_RESPONSE_TIME, totalResponseTime);
//	responseTimeValues.put(HttpRequstResultProvider.COLUMN_AVERAGE_RESPONSE_TIME, averageResponseTime);
//	responseTimeValues.put(HttpRequstResultProvider.COLUMN_NUM_OF_REQUEST, models.size());
//	responseTimeValues.put(HttpRequstResultProvider.COLUMN_CREATED_AT, currentDate);
//	responseTimeValues.put(HttpRequstResultProvider.COLUMN_UPDATED_AT, currentDate);
//	resolver.insert(HttpRequstResultProvider.RESPONSE_TIME_CONTENT_URI, responseTimeValues);
}

private void insertHttpRecord(ContentResolver resolver, String uuid, String currentDate) {
	for (HttpRequestModel model : models) {
		ContentValues values = new ContentValues();

//		values.put(HttpRequstResultProvider.COLUMN_INCLUDE_API, model.getWithAPI());
//		values.put(HttpRequstResultProvider.COLUMN_REQUEST_METHOD, model.getRequestMethod());
//		values.put(HttpRequstResultProvider.COLUMN_RESPONSE_CODE, model.getResponseCode());
//		values.put(HttpRequstResultProvider.COLUMN_URL, model.getRequestUrl());
//		values.put(HttpRequstResultProvider.COLUMN_RESPONE_TIME, model.getResponseTime());
//		values.put(HttpRequstResultProvider.COLUMN_RESPONE_CONTENT, saveResponseContent ? model.getContent() : "");
//		values.put(HttpRequstResultProvider.COLUMN_RESPONSE_TIME_ID, uuid);
//		values.put(HttpRequstResultProvider.COLUMN_IS_HTTPS, https);
//		values.put(HttpRequstResultProvider.COLUMN_CREATED_AT, currentDate);
//		values.put(HttpRequstResultProvider.COLUMN_UPDATED_AT, currentDate);
//		resolver.insert(HttpRequstResultProvider.HTTP_CONTENT_URI, values);
	}
}
}

public boolean isHttp() {
	return https;
}

public void setHttp(boolean http) {
	this.https = http;
}

}
