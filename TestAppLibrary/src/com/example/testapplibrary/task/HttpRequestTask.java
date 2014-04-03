package com.example.testapplibrary.task;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.testapplibrary.model.HttpRequestModel;

public class HttpRequestTask implements Callable<List<HttpRequestModel>>, Runnable {

public static final String TAG_ON_POST_EXECUTE = "HttpRequestAsyncTask.onPostExecute";

public static final String RESULT_TEMPLE_WITH_CONTENT = "%s - %s \n response time : %s \n response content : %s";

public static final String RESULT_TEMPLE = "%s - %s \n response time : %s \n";

protected List<String> result = new ArrayList<String>();

protected List<HttpRequestModel> models = new ArrayList<HttpRequestModel>();

private List<PostActionInterface> postActionList;

protected Context ctx;

protected List<String> urls;

protected boolean saveToDb;

protected boolean saveResponseContent;

protected boolean https;

public HttpRequestTask(Context ctx, List<String> urls) {
	this(ctx, urls, false);
}

public HttpRequestTask(Context ctx, List<String> urls, boolean saveToDb) {
	this(ctx, urls, saveToDb, false);
}

public HttpRequestTask(Context ctx, List<String> urls, boolean saveToDb, boolean saveResponseContent) {
	this(ctx, urls, saveToDb, saveResponseContent, false);
}

public HttpRequestTask(Context ctx, List<String> urls, boolean saveToDb, boolean saveResponseContent, boolean https) {
	super();
	this.ctx = ctx;
	this.urls = urls;
	this.saveToDb = saveToDb;
	this.saveResponseContent = saveResponseContent;
	this.https = https;
	postActionList = new ArrayList<PostActionInterface>();
}

@Override
public List<HttpRequestModel> call() throws Exception {
	execute();
	return models;
}

@Override
public void run() {
	execute();
}

private void execute() {
	executeRequests();
	postCall(models, result);
}

private void executeRequests() {
	ExecutorService service = Executors.newFixedThreadPool(10);
	List<Future<HttpRequestModel>> futures = new ArrayList<Future<HttpRequestModel>>();
	try {
		if (urls != null) {
			for (String url : urls) {
				futures.add(service.submit(new CommonHttpRequestTask("GET", url)));
			}
			for (Future<HttpRequestModel> future : futures) {
				try {
					HttpRequestModel model = future.get(20, TimeUnit.SECONDS);
					models.add(model);
					result.add(String.format(RESULT_TEMPLE, model.getRequestUrl(),
									String.valueOf(model.getResponseCode()), String.valueOf(model.getResponseTime())));
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					Log.d("HttpRequestTask", "time out to get response");
				}
			}
		}
	} finally {
		if (service != null) {
			service.shutdownNow();
		}
	}
}

public void postCall(List<HttpRequestModel> models, List<String> result) {
	for (PostActionInterface post : postActionList) {
		post.execute(models);
	}
}


public void addPostAction(PostActionInterface postAction) {
	this.postActionList.add(postAction);
}

}
