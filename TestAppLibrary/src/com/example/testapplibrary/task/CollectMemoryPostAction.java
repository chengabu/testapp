package com.example.testapplibrary.task;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.example.testapplibrary.custom.handler.UpdateViewTextHandler;
import com.example.testapplibrary.model.HttpRequestModel;

public class CollectMemoryPostAction implements PostActionInterface {

private Context context;
private UpdateViewTextHandler handler;
private boolean withApi;

public CollectMemoryPostAction(Context contex, UpdateViewTextHandler handler, boolean withApi) {
	super();
	this.context = contex;
	this.handler = handler;
	this.withApi = withApi;
}

public CollectMemoryPostAction(Context contex, UpdateViewTextHandler handler) {
	this(contex, handler, false);
}

@Override
public void execute(List<HttpRequestModel> models) {
	Log.d("CollectMemoryPostAction", "CollectMemoryPostAction execute.");
	new Thread(new CollectMemInfoTask(context, handler, withApi)).start();

}

}
