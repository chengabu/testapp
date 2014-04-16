package com.example.testapplibrary.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;


public class InsertDbTask implements Callable<Boolean> {
	private Context context;
	private List<ContentValues> values = new ArrayList<ContentValues>();
	private Uri uri;
	
	public InsertDbTask(Context context, List<ContentValues> values, Uri uri) {
		super();
		this.context = context;
		this.values = values;
		this.uri = uri;
	}
	public InsertDbTask(Context context, ContentValues values, Uri uri) {
		super();
		this.context = context;
		this.values.add(values);
		this.uri = uri;
	}
	
	@Override
	public Boolean call() throws Exception {
		ContentResolver resover = context.getContentResolver();
		for (ContentValues value : values) {
			resover.insert(uri, value);
		}
		values.clear();
		return true;
	}
	
}