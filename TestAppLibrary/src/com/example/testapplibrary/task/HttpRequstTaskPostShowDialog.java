package com.example.testapplibrary.task;

import java.math.BigDecimal;
import java.util.List;

import com.example.testapplibrary.model.HttpRequestModel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

public class HttpRequstTaskPostShowDialog extends HttpRequestTask {

private Handler handler;

public HttpRequstTaskPostShowDialog(Context ctx, List<String> urls, boolean saveToDb, boolean saveResponseContent,
									boolean https, Handler handler) {
	super(ctx, urls, saveToDb, saveResponseContent, https);
	this.handler = handler;
}

public HttpRequstTaskPostShowDialog(Context ctx, List<String> urls, boolean saveToDb, boolean saveResponseContent, Handler handler) {
	super(ctx, urls, saveToDb, saveResponseContent);
	this.handler = handler;
}

public HttpRequstTaskPostShowDialog(Context ctx, List<String> urls, boolean saveToDb, Handler handler) {
	super(ctx, urls, saveToDb);
	this.handler = handler;
}

public HttpRequstTaskPostShowDialog(Context ctx, List<String> urls, Handler handler) {
	super(ctx, urls);
	this.handler = handler;
}

@Override
public void postCall(List<HttpRequestModel> models, List<String> results) {
	super.postCall(models, results);
	long totalResponseTime = 0;
	for (HttpRequestModel model : models) {
		totalResponseTime += model.getResponseTime();
	}

	String averageResponseTimeStr = "";
	final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
	builder.setTitle("Average Response Time");
	if (models.size() > 0) {
		averageResponseTimeStr = new BigDecimal(totalResponseTime).divide(new BigDecimal(models.size()), 2,
						BigDecimal.ROUND_HALF_UP).toString();
		builder.setMessage("" + averageResponseTimeStr + "(ms)");
	} else {
		builder.setMessage("No requests are made.");
	}
	
	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int arg1) {
			dialog.dismiss();
		}
	});
	
	handler.post(new Runnable() {
		
		@Override
		public void run() {
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	});
}
	
	

}
