package com.example.testapplibrary.model;

public class HttpRequestModel {

private int responseCode;

private String responseMessage;

private String content;

private String requestMethod;

private long responseTime;

private int withAPI;

private String requestUrl;

public long getResponseTime() {
	return responseTime;
}

public HttpRequestModel(int responseCode, String responseMessage, String content, long responseTime) {
	super();
	this.responseCode = responseCode;
	this.responseMessage = responseMessage;
	this.content = content;
	this.responseTime = responseTime;
}

public void setResponseTime(long responseTime) {
	this.responseTime = responseTime;
}

public void setRequestMethod(String requestMethod) {
	this.requestMethod = requestMethod;
}

public HttpRequestModel(int responseCode, String responseMessage, String content) {
	super();
	this.responseCode = responseCode;
	this.responseMessage = responseMessage;
	this.content = content;
}

public int getResponseCode() {
	return responseCode;
}

public String getResponseMessage() {
	return responseMessage;
}

public String getContent() {
	return content;
}

public String getRequestMethod() {
	return requestMethod;
}

public int getWithAPI() {
	return withAPI;
}

public void setWithAPI(int withAPI) {
	this.withAPI = withAPI;
}

public String getRequestUrl() {
	return requestUrl;
}

public void setRequestUrl(String requestUrl) {
	this.requestUrl = requestUrl;
}

}
