package com.example.moblie.withoutapi;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.testapplibrary.task.HttpRequestTask;
import com.example.testapplibrary.task.HttpRequstTaskPostShowDialog;
import com.example.testapplibrary.task.InsertRecordPostAction;
import com.example.testapplibrary.task.ShowDialogPostAction;
import com.example.testapplibrary.task.ShowHistoryResponseTimeTask;
import com.example.testapplibrary.task.UpdateAdapterAction;

public class HttpRequestWithoutAPI extends Activity {

// public static final String URL = "http://10.30.178.121:8080/";
public static final String URL = "http://10.30.178.14:8080/ResponseSystemTimeServlet";
public static final int DEFAULT_NUMBER_OF_URL = 10;
public static final String TAG_INIT_URL = "HttpRequestWithoutAPI.iniUrls";
public static final String TAG_SHOW_HISTORY = "HttpRequestWithoutAPI.showHistoryResponseTime";
private int numberOfRequest;
private boolean https;
private Handler handler;

// private EditText numOfUrlHeader;
// private EditText urlTextHeader;
// private EditText numOfUrlFooter;
// private EditText urlTextFooter;

@Override
protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	ListView listView = (ListView) this.findViewById(R.id.mainListView);
	final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.http_without_api_list_item,
					R.id.urlAndStatus);

	handler = new Handler();
	final ArrayList<String> urls = new ArrayList<String>();
	Bundle bundle = this.getIntent().getExtras();
	https = bundle.getBoolean(MainActivity.CONFIG_IS_HTTPs, false);
	numberOfRequest = bundle.getInt(MainActivity.CONFIG_NUMBER_OF_REQUEST);
	numberOfRequest = numberOfRequest == 0 ? DEFAULT_NUMBER_OF_URL : numberOfRequest;
	String[] domains = bundle.getStringArray(MainActivity.CONFIG_URLS);
	domains = domains == null ? this.getResources().getStringArray(R.array.differentDomainUrls) : domains;

	initUrls(urls, domains, numberOfRequest);
	

	initHeader(listView, adapter, urls);

	initFooter(listView, adapter, urls);

	listView.setAdapter(adapter);
	
	executeHttpRequestTask(adapter, urls);
//	mTask = new HttpRequestAsyncTask(HttpRequestWithoutAPI.this, adapter, true, false, https);
//	mTask.execute(urls);
	// HttpRequestAsyncTask task = new
	// HttpRequestAsyncTask(HttpRequestWithoutAPI.this, adapter, true);
	// new AutoSendHttpRequestAsyncTask(task, urls, 5 * 60 * 1000, new
	// Handler()).execute(true);
}

private void executeHttpRequestTask(final ArrayAdapter<String> adapter, final List<String> urls) {
	UpdateAdapterAction updateAdapterAction = new UpdateAdapterAction(adapter, handler);
	ShowDialogPostAction showDialogPostAction = new ShowDialogPostAction(handler, HttpRequestWithoutAPI.this);
	InsertRecordPostAction insertRecordPostAction = new InsertRecordPostAction(this, true, https, false);
	HttpRequestTask task = new HttpRequestTask(HttpRequestWithoutAPI.this, urls, true, false, https);
	task.addPostAction(updateAdapterAction);
	task.addPostAction(showDialogPostAction);
	task.addPostAction(insertRecordPostAction);
	new Thread(task).start();
}

private void initFooter(ListView listView, final ArrayAdapter<String> adapter, final List<String> urls) {
	View footerView = getLayoutInflater().inflate(R.layout.load_more_footer, null);
	listView.addFooterView(footerView);
	Button loadMoreButton = (Button) footerView.findViewById(R.id.loadMoreFotter);
	loadMoreButton.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			executeHttpRequestTask(adapter, urls);
		}
	});
}

private void initHeader(ListView listView, final ArrayAdapter<String> adapter, final List<String> urls) {

	View headerView = getLayoutInflater().inflate(R.layout.load_more_header, null);
	listView.addHeaderView(headerView);
	Button loadMoreButtonHeader = (Button) headerView.findViewById(R.id.loadMoreheader);

	// numOfUrlHeader = (EditText) headerView.findViewById(R.id.count);
	// urlTextHeader = (EditText) headerView.findViewById(R.id.path);
	// numOfUrlHeader.setText(DEFAULT_NUMBER_OF_URL);
	// urlTextHeader.setText(URL);

	loadMoreButtonHeader.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			new Thread(new HttpRequstTaskPostShowDialog(HttpRequestWithoutAPI.this, urls, true, false, https, handler))
			.start();
			Toast.makeText(HttpRequestWithoutAPI.this, "New data have been add to the end of list view", 5000).show();
		}
	});

	Button historyButtor = (Button) headerView.findViewById(R.id.HistoryResponseTime);
	historyButtor.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View view) {
			new ShowHistoryResponseTimeTask(HttpRequestWithoutAPI.this).execute(true);
		}
	});

}

private void initUrls(List<String> urls, String[] domains, int numberOfUrls) {
	for (String domain : domains) {
		for (int i = 1; i <= numberOfUrls; i++) {
			String url = https ? domain : domain + i;
			Log.d(TAG_INIT_URL, url);
			urls.add(url);
		}
	}
}
}
