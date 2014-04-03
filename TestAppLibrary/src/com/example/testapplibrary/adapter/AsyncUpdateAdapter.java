package com.example.testapplibrary.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.testapplibrary.task.AsyncUpdateHttpRequestWithoutAPI;
import com.example.testapplibrary.task.GetHttpRequest;

public class AsyncUpdateAdapter extends BaseAdapter {

	public static final String BUNDLE_RESULT_KEY = "httpRequestResultStr";

	private Context mContext;
	private List<String> urlStrs = new ArrayList<String>();
	private int mLayoutResId;
	private int mTextViewId;
	private ExecutorService mExecutorService;
	private AtomicInteger counter;
	private Map<Integer, String> cacheMap = new ConcurrentHashMap<Integer, String>();
	private List<ContentValues> contentValues = new ArrayList<ContentValues>();

	public AsyncUpdateAdapter(Context mContext, List<String> urlStrs,
			int mLayoutResId, int mTextViewId,
			ExecutorService mExecutorService, AtomicInteger counter) {
		super();
		this.mContext = mContext;
		this.urlStrs = urlStrs;
		this.mLayoutResId = mLayoutResId;
		this.mTextViewId = mTextViewId;
		this.mExecutorService = mExecutorService;
		this.counter = counter;
	}

	@Override
	public int getCount() {
		return urlStrs.size();
	}

	@Override
	public Object getItem(int position) {
		return urlStrs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		TextView textView;
		if (convertView == null) {
			view = ((Activity) mContext).getLayoutInflater().inflate(
					mLayoutResId, null);
		} else {
			view = convertView;
		}

		textView = (TextView) view.findViewById(mTextViewId);
		String httpUrlStr = urlStrs.get(position);
		if (cacheMap.get(position) == null) {
			textView.setText("Processing the request for " + httpUrlStr + "...");
			mExecutorService.submit(new GetHttpRequest(new AsyncUpdateHandler(
					cacheMap, position), httpUrlStr, textView, counter,
					mContext, mExecutorService, contentValues));
		} else {
			textView.setText(cacheMap.get(position));
		}
		Log.d("getView", "" + position + " getview is invoke");

		return view;
	}

	public void pullDownRefresh() {
		Log.d("pullDownRefresh.counter", "" + counter.get());
		if (counter.get() == 0) {

			Map<Integer, String> newCacheMap = new ConcurrentHashMap<Integer, String>();
			Log.d("pullDownRefresh", "Start pullDownRefresh");
			int pullDownCount = AsyncUpdateHttpRequestWithoutAPI.PULL_DOWN_COUNT;
			for (Integer key : cacheMap.keySet()) {
				String value = cacheMap.get(key);
				Log.d("pullDownRefresh", "Key : " + (key + pullDownCount)
						+ " , value : " + value);
				newCacheMap.put(key + pullDownCount, value);
			}
			if (!newCacheMap.isEmpty()) {
				cacheMap.clear();
				cacheMap.putAll(newCacheMap);
			}
			for (int i = 0; i < pullDownCount; i++) {
				urlStrs.add(0, AsyncUpdateHttpRequestWithoutAPI.URL);
			}
			this.notifyDataSetChanged();
			Log.d("pullDownRefresh", "End pullDownRefresh");
		}
	}

	public boolean isEmpty() {
		return cacheMap.isEmpty();
	}
	
	public List<ContentValues> getContentValues() {
		return contentValues;
	}

}

class AsyncUpdateHandler extends Handler {

	private Map<Integer, String> map;
	private int position;

	public AsyncUpdateHandler(Map<Integer, String> map, int position) {
		super();
		this.map = map;
		this.position = position;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		TextView textView = (TextView) msg.obj;
		Bundle bundle = msg.getData();
		String responseStr = bundle
				.getString(AsyncUpdateAdapter.BUNDLE_RESULT_KEY);
		textView.setText(responseStr);
		map.put(position, responseStr);
	}
}