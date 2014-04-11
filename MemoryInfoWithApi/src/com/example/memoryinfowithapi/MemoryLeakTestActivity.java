package com.example.memoryinfowithapi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dell.app.FoglightAPM.FoglightAPM;
import com.dell.app.FoglightAPM.annotation.FogAnnotation;
import com.example.testapplibrary.custom.handler.UpdateViewTextHandler;
import com.example.testapplibrary.task.CollectMemoryInfoAfterHttpRequestTask;

@FogAnnotation(
				// beaconURL =
				// "http://10.8.252.236/wordpress/archiverProxy?op=uploadhitdata"
				beaconURL = "http://10.8.255.236:7630/archiverProxy?op=uploadhitdata"
// beaconURL =
// "http://10.8.252.230/wordpress/archiverProxy.php?op=uploadhitdata"
)
public class MemoryLeakTestActivity extends Activity {

@Override
protected void onCreate(Bundle savedInstanceState) {
	FoglightAPM.initialize(this, "MemoryLeakTestActivity", "1");
	super.onCreate(savedInstanceState);
	FoglightAPM.recordAppScreenStart("MemoryLeakTestActivity");
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
