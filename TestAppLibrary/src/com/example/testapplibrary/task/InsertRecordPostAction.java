package com.example.testapplibrary.task;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.testapplibrary.database.HttpRequstResultProvider;
import com.example.testapplibrary.model.HttpRequestModel;

public class InsertRecordPostAction implements PostActionInterface {

private static final String TAG_ON_POST_EXECUTE = "InsertRecordPostAction.saveToDBAction";
private Context ctx;
private boolean saveContent;
private boolean https;
private boolean includeAPI;

public InsertRecordPostAction(Context ctx, boolean saveContent, boolean https, boolean includeAPI) {
	super();
	this.ctx = ctx;
	this.saveContent = saveContent;
	this.https = https;
	this.includeAPI = includeAPI;
}

@Override
public void execute(List<HttpRequestModel> models) {
	saveToDBAction(models);
}

public void saveToDBAction(List<HttpRequestModel> models) {
	ContentResolver resolver = ctx.getContentResolver();

	ContentValues responseTimeValues = new ContentValues();
	String uuid = UUID.randomUUID().toString();
	String currentDate = "";
	try {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		currentDate = format.format(new Date());
	} catch (Exception e) {
		Log.e("SaveToDbTask", "Failed to format date", e);
	}

	insertResponseTimeRecord(resolver, models, responseTimeValues, uuid, currentDate);

	insertHttpRecord(resolver, models, uuid, currentDate);
	Log.d(TAG_ON_POST_EXECUTE, "Finish save data to db");
}

private void insertResponseTimeRecord(ContentResolver resolver, List<HttpRequestModel> models,
				ContentValues responseTimeValues,
				String uuid, String currentDate) {
	long totalResponseTime = 0;
	double averageResponseTime = 0;

	for (HttpRequestModel model : models) {
		totalResponseTime += model.getResponseTime();
	}

	if (models.size() > 0) {
		averageResponseTime = new BigDecimal(totalResponseTime).divide(new BigDecimal(models.size()), 2,
						BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	responseTimeValues.put(HttpRequstResultProvider.COLUMN_ID, uuid);
	responseTimeValues.put(HttpRequstResultProvider.COLUMN_TOTAL_RESPONSE_TIME, totalResponseTime);
	responseTimeValues.put(HttpRequstResultProvider.COLUMN_AVERAGE_RESPONSE_TIME, averageResponseTime);
	responseTimeValues.put(HttpRequstResultProvider.COLUMN_NUM_OF_REQUEST, models.size());
	responseTimeValues.put(HttpRequstResultProvider.COLUMN_CREATED_AT, currentDate);
	responseTimeValues.put(HttpRequstResultProvider.COLUMN_UPDATED_AT, currentDate);
	resolver.insert(HttpRequstResultProvider.RESPONSE_TIME_CONTENT_URI, responseTimeValues);
}

private void insertHttpRecord(ContentResolver resolver, List<HttpRequestModel> models, String uuid, String currentDate) {
	for (HttpRequestModel model : models) {
		ContentValues values = new ContentValues();

		values.put(HttpRequstResultProvider.COLUMN_INCLUDE_API, model.getWithAPI());
		values.put(HttpRequstResultProvider.COLUMN_REQUEST_METHOD, model.getRequestMethod());
		values.put(HttpRequstResultProvider.COLUMN_RESPONSE_CODE, model.getResponseCode());
		values.put(HttpRequstResultProvider.COLUMN_URL, model.getRequestUrl());
		values.put(HttpRequstResultProvider.COLUMN_RESPONE_TIME, model.getResponseTime());
		values.put(HttpRequstResultProvider.COLUMN_RESPONE_CONTENT, saveContent ? model.getContent() : "");
		values.put(HttpRequstResultProvider.COLUMN_RESPONSE_TIME_ID, uuid);
		values.put(HttpRequstResultProvider.COLUMN_IS_HTTPS, https);
		values.put(HttpRequstResultProvider.COLUMN_INCLUDE_API, includeAPI);
		values.put(HttpRequstResultProvider.COLUMN_CREATED_AT, currentDate);
		values.put(HttpRequstResultProvider.COLUMN_UPDATED_AT, currentDate);
		resolver.insert(HttpRequstResultProvider.HTTP_CONTENT_URI, values);
	}
}

}
