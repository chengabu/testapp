package com.example.memoryinfowithoutapi;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.example.testapplibrary.custom.handler.UpdateViewTextHandler;
import com.example.testapplibrary.task.CollectMemoryPostAction;
import com.example.testapplibrary.task.HttpRequestTask;
import com.example.testapplibrary.task.UpdateHttpViewPostAction;

public class MainActivity extends Activity {

@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(com.example.testapplibrary.R.layout.memory_info_main);
	initExecuteTask();
	
}

private void initExecuteTask() {
	TextView httpView = (TextView) this.findViewById(R.id.http_text_view);
	TextView memoryView = (TextView) this.findViewById(R.id.memory_text_view);
	
	UpdateViewTextHandler httpHandler = new UpdateViewTextHandler(httpView);
	UpdateViewTextHandler memoryHandler = new UpdateViewTextHandler(memoryView);
	
	List<String> urls = new ArrayList<String>();
	String[] httpUrls = this.getResources().getStringArray(com.example.testapplibrary.R.array.differentDomainUrls);
	String[] httpsUrls = this.getResources().getStringArray(com.example.testapplibrary.R.array.differentDomainHttpsUrls);
	addToList(urls, httpUrls);
	addToList(urls, httpsUrls);
	
	HttpRequestTask task = new HttpRequestTask(this, urls);
	UpdateHttpViewPostAction updateHttpViewPostAction = new UpdateHttpViewPostAction(httpHandler);
	CollectMemoryPostAction collectMemoryPostAction = new CollectMemoryPostAction(this, memoryHandler);
	task.addPostAction(updateHttpViewPostAction);
	task.addPostAction(collectMemoryPostAction);
	
	new Thread(task).start();
}

private void addToList(List<String> urls, String[] urlArray) {
	for (String url : urlArray) {
		urls.add(url);
	}
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
}

}
