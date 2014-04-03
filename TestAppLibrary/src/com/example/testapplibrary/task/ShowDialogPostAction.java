package com.example.testapplibrary.task;

import java.math.BigDecimal;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import com.example.testapplibrary.model.HttpRequestModel;

public class ShowDialogPostAction implements PostActionInterface {

	private Handler handler;
	
	private Context ctx;

	public ShowDialogPostAction(Handler handler, Context ctx) {
		super();
		this.handler = handler;
		this.ctx = ctx;
	}

	@Override
	public void execute(List<HttpRequestModel> models) {
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
