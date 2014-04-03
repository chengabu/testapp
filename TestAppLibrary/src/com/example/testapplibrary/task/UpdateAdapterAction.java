package com.example.testapplibrary.task;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.testapplibrary.model.HttpRequestModel;

public class UpdateAdapterAction implements PostActionInterface {

private static final String RESULT_TEMPLE = "%s - %s \n response time : %s \n";

private ArrayAdapter<String> adapter;
private Handler handler;

public UpdateAdapterAction(ArrayAdapter<String> adapter, Handler handler) {
	super();
	this.handler = handler;
	this.adapter = adapter;
}

@Override
public void execute(final List<HttpRequestModel> models) {
	handler.post(new Runnable() {
		
		@Override
		public void run() {
			List<String> results = new ArrayList<String>();
			for (HttpRequestModel model : models) {
				results.add(String.format(RESULT_TEMPLE, model.getRequestUrl(),
								String.valueOf(model.getResponseCode()), String.valueOf(model.getResponseTime())));
			}
			for (String result : results) {
				adapter.add(result);
			}
			adapter.notifyDataSetChanged();
		}
	});
	Log.d("UpdateAdapterAction", "Finish notifyDataSetChanged");
}

}
