package com.example.memoryinfowithoutapi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.testapplibrary.custom.handler.UpdateViewTextHandler;
import com.example.testapplibrary.task.CollectMemoryInfoAfterHttpRequestTask;

public class MemoryLeakTestActivity extends Activity {

@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.memory_info_main);

	TextView httpView = (TextView) this.findViewById(R.id.http_text_view);
	httpView.setVisibility(View.INVISIBLE);

	TextView memoryView = (TextView) this.findViewById(R.id.memory_text_view);

	UpdateViewTextHandler memoryHandler = new UpdateViewTextHandler(memoryView);
	CollectMemoryInfoAfterHttpRequestTask task = new CollectMemoryInfoAfterHttpRequestTask(this, memoryHandler);
	task.setPostSleepTime(30 * 1000);
	new Thread(task).start();

}

@Override
protected void onDestroy() {
	super.onDestroy();
}

}
