package com.example.moblieapmtestdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.testapplibrary.adapter.AsyncUpdateAdapter;
import com.example.testapplibrary.task.InsertDbTask;


public class AsyncUpdateHttpRequestWithoutAPI extends Activity implements
				OnTouchListener {

// public static final String URL = "http://10.30.178.121:8080/";
 public static String URL = "http://10.30.178.14:8080/examples/servlets/";

//public static String URL = "https://www.google.com.hk/search?q=1";
public static final int INIT_COUNT = 10;

public static final int PULL_DOWN_COUNT = 10;

private AtomicInteger counter;

private ExecutorService executorService;

private LinearLayout mHeaderView;

private LinearLayout mFooterView;

private AsyncUpdateAdapter adapter;

private float startY = 0;

private boolean refresh = false;

@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	ListView listView = (ListView) this.findViewById(R.id.mainListView);
	listView.setOnTouchListener(this);
	mHeaderView = (LinearLayout) this.getLayoutInflater().inflate(
					R.layout.list_view_header, null);
	mFooterView = (LinearLayout) this.getLayoutInflater().inflate(
					R.layout.list_view_footer, null);
	listView.addHeaderView(mHeaderView);
	listView.addFooterView(mFooterView);

	counter = new AtomicInteger();
	executorService = Executors.newFixedThreadPool(10);

	List<String> urls = new ArrayList<String>();
	for (int i = 0; i < INIT_COUNT; i++) {
		urls.add(URL);
	}
	adapter = new AsyncUpdateAdapter(this, urls, R.layout.http_without_api_list_item, R.id.urlAndStatus,
					executorService, counter);
	listView.setAdapter(adapter);
	// Handler hander = new Handler();
	// executorService.submit(new AutoTriggerNewData(adapter, counter,
	// hander));
	View button = this.findViewById(R.id.headerButton);
	button.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			ProgressDialog dialog = new ProgressDialog(AsyncUpdateHttpRequestWithoutAPI.this);
			dialog.setCancelable(false);
			dialog.setMessage("Saving data...");
			dialog.show();
			Future<Boolean> furture = executorService.submit(new InsertDbTask(AsyncUpdateHttpRequestWithoutAPI.this,
							adapter.getContentValues()));
			try {
				furture.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				dialog.dismiss();
			}

		}
	});

}

@Override
protected void onDestroy() {
	super.onDestroy();
	if (executorService != null) {
		executorService.shutdownNow();
	}
}

@Override
public boolean onTouch(View v, MotionEvent event) {
	ListView listView = (ListView) v;
	switch (event.getAction()) {
	case MotionEvent.ACTION_DOWN:
		startY = event.getRawY();
		Log.d("onTouch", "ACTION_DOWN trigger!");
		break;
	case MotionEvent.ACTION_MOVE: {
		if (listView.getFirstVisiblePosition() == 0
			&& (event.getRawY() - startY) > 50) {
			LayoutParams params = mHeaderView.getLayoutParams();
			params.height = 50;
			mHeaderView.setLayoutParams(params);
			refresh = true;
		}
		Log.d("onTouch", "ACTION_MOVE trigger!");
	}
		break;
	case MotionEvent.ACTION_UP: {
		if (refresh) {
			refresh = false;
			startY = 0;
			((AsyncUpdateAdapter) ((HeaderViewListAdapter) listView.getAdapter()).getWrappedAdapter())
							.pullDownRefresh();
		}
		Log.d("onTouch", "Action Up trigger!");
	}
	}
	return super.onTouchEvent(event);
}

}

class AutoTriggerNewData implements Callable<Boolean> {
private AsyncUpdateAdapter mAdapter;

private AtomicInteger counter;

private Handler handler;

public AutoTriggerNewData(AsyncUpdateAdapter mAdapter,
							AtomicInteger counter, Handler handler) {
	super();
	this.mAdapter = mAdapter;
	this.counter = counter;
	this.handler = handler;
}

@Override
public Boolean call() throws Exception {

	loop();
	AsyncUpdateHttpRequestWithoutAPI.URL = "https://www.google.com.hk/search?q=1";
	loop();
	return true;
}

private void loop() throws InterruptedException {
	Log.d("loop", "going to execute loop");
	int i = 0;
	while (i <= 10000) {
		if (!mAdapter.isEmpty() && counter.get() == 0) {
			Log.d("loop", "enter execute loop");
			handler.post(new Runnable() {
				@Override
				public void run() {
					Log.d("loop", "post message");
					mAdapter.pullDownRefresh();
				}
			});
			i++;
			Thread.sleep(1000 * 20);
		}
	}
}
}