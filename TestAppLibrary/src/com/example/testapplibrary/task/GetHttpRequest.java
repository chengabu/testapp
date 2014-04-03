package com.example.testapplibrary.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.example.testapplibrary.adapter.AsyncUpdateAdapter;

public class GetHttpRequest implements Callable<Long> {

	private Handler handler;
	private String url;
	private TextView textview;
	private AtomicInteger counter;
	private Context context;
	List<ContentValues> contentValues;

	public GetHttpRequest(Handler handler, String url, TextView textview,
			AtomicInteger counter, Context context,
			ExecutorService executorService, List<ContentValues> contentValues) {
		super();
		this.handler = handler;
		this.url = url;
		this.textview = textview;
		this.counter = counter;
		this.context = context;
		this.contentValues = contentValues;
	}

	@Override
	public Long call() throws Exception {
		counter.incrementAndGet();
		try {
			Long responseTime = null;
			try {
				String method = "GET";
				int isIncludeApi = 0;
				URL u = new URL(url);
				HttpURLConnection con = (HttpURLConnection) u.openConnection();
				con.setUseCaches(false);
				con.setRequestMethod(method);
//				con.addRequestProperty("Cache-Control", "no-cache");
				long startTime = System.currentTimeMillis();
				con.connect();
				responseTime = System.currentTimeMillis() - startTime;
				int responseCode = con.getResponseCode();
				con.disconnect();
				Message message = new Message();
				message.obj = textview;
				Bundle bundle = message.getData();
				bundle.putString(AsyncUpdateAdapter.BUNDLE_RESULT_KEY, url + " - "
						+ responseCode + "\t response time : " + responseTime);
				handler.sendMessage(message);
				
				ContentValues values = new ContentValues();
//				values.put(HttpRequstResultProvider.COLUMN_INCLUDE_API, isIncludeApi);
//				values.put(HttpRequstResultProvider.COLUMN_REQUEST_METHOD, method);
//				values.put(HttpRequstResultProvider.COLUMN_RESPONSE_CODE, responseCode);
//				values.put(HttpRequstResultProvider.COLUMN_URL, url);
//				values.put(HttpRequstResultProvider.COLUMN_RESPONE_TIME, responseTime);
				contentValues.add(values);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return responseTime;
		} finally {
			counter.decrementAndGet();
		}
	}
	
	private synchronized void getRawConnectionData(HttpURLConnection connect, Long responseTime) {
		
		StringBuffer buffer = new StringBuffer();
		for (String requestKey : connect.getRequestProperties().keySet()) {
			buffer.append("Request properties - " + requestKey + ":\n");
			for (String value : connect.getRequestProperties().get(requestKey)) {
				buffer.append(value + "\n");
			}
		}
		
		try {
			int responseCode = connect.getResponseCode();
			buffer.append("Response code : " + responseCode + "\n");
			String responseMsg = connect.getResponseMessage();
			buffer.append(responseMsg + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		buffer.append("Response time : " + responseTime + "\n");
		
		String fileName = "HttpRequestRawData.txt";
		File file  = new File(context.getFilesDir(), fileName);
		OutputStream os = null;
		try {
			os = context.openFileOutput("HttpRequestRawData.txt", Context.MODE_APPEND);
			os.write(buffer.toString().getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}
