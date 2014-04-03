package com.example.testapplibrary.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public abstract class ProgressAsyncTask<P, T, R> extends AsyncTask<P, T, R>{

	private ProgressDialog dialog;
	private Context ctx;
	private String message = "Sending requests...";
	
	public ProgressAsyncTask(Context ctx) {
		dialog = new ProgressDialog(ctx);
		this.ctx = ctx;
	}

	@Override
	abstract protected R doInBackground(P... params);

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog.setMessage(message);
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected void onPostExecute(R result) {
		super.onPostExecute(result);
		dialog.dismiss();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Context getCtx() {
		return ctx;
	}
}
