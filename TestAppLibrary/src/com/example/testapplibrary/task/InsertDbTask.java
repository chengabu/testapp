package com.example.testapplibrary.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;


public class InsertDbTask implements Callable<Boolean> {
	private Context context;
	private List<ContentValues> values = new ArrayList<ContentValues>();
	public InsertDbTask(Context context, ContentValues values) {
		super();
		this.context = context;
		this.values.add(values);
	}
	public InsertDbTask(Context context, List<ContentValues> values) {
		super();
		this.context = context;
		this.values = values;
	}
	
	@Override
	public Boolean call() throws Exception {
		ContentResolver resover = context.getContentResolver();
		for (ContentValues value : values) {
//			resover.insert(HttpRequstResultProvider.HTTP_CONTENT_URI, value);
		}
		values.clear();
		return true;
	}
	
}