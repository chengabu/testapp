package com.example.testapplibrary.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import android.util.Log;

import com.example.testapplibrary.model.HttpRequestModel;

public class CommonHttpRequestTask implements Callable<HttpRequestModel> {

private String requestMedthod;
private String requestUrl;
private boolean useCache;
private boolean isHttps;
private int withAPI;
private boolean parseContent;

public CommonHttpRequestTask(String requestMedthod, String requestUrl) {
	super();
	this.requestMedthod = requestMedthod;
	this.requestUrl = requestUrl;
}


@Override
public HttpRequestModel call() throws Exception {
	
	Log.d("CommonHttpRequestTask", "Sending http request "+ requestUrl);
	HttpRequestModel model = null;
	Long responseTime = null;
		
	String method = requestMedthod == null ? "GET" : requestMedthod;
	URL u = new URL(requestUrl);
	HttpURLConnection con = null;
	try {
		con = (HttpURLConnection) u.openConnection();
		con.setUseCaches(useCache);
		con.setRequestMethod(method);
		con.setConnectTimeout(10 * 1000);
		long startTime = System.currentTimeMillis();
		con.connect();
		responseTime = System.currentTimeMillis() - startTime;
		int responseCode = con.getResponseCode();
		String responseMessage = con.getResponseMessage();
		
		model = getResultModel(responseTime, method, con, responseCode, responseMessage);
		
	} finally {
		if (con != null) {
			con.disconnect();
			con = null;
		}
	}
	
	Log.d("CommonHttpRequestTask", "Finish http request "+ requestUrl);
	
	return model;
}

private HttpRequestModel getResultModel(Long responseTime, String method, HttpURLConnection con, int responseCode,
				String responseMessage) throws IOException {
	HttpRequestModel model = null;;
	StringBuffer buffer = new StringBuffer();
	if (parseContent) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line = null;
		while ((line = reader.readLine()) != null) {
			buffer.append(line).append("\n");
		}
	}
	String content = buffer.toString();
	model = new HttpRequestModel(responseCode, responseMessage, content, responseTime);
	model.setRequestMethod(method);
	model.setWithAPI(withAPI);
	model.setRequestUrl(requestUrl);
	return model;
}

public String getRequestMedthod() {
	return requestMedthod;
}

public void setRequestMedthod(String requestMedthod) {
	this.requestMedthod = requestMedthod;
}

public String getRequestUrl() {
	return requestUrl;
}

public void setRequestUrl(String requestUrl) {
	this.requestUrl = requestUrl;
}

public boolean isUseCache() {
	return useCache;
}

public void setUseCache(boolean useCache) {
	this.useCache = useCache;
}

public boolean isHttps() {
	return isHttps;
}

public void setHttps(boolean isHttps) {
	this.isHttps = isHttps;
}

public int getWithAPI() {
	return withAPI;
}

public void setWithAPI(int withAPI) {
	this.withAPI = withAPI;
}


public boolean isParseContent() {
	return parseContent;
}


public void setParseContent(boolean parseContent) {
	this.parseContent = parseContent;
}

}
