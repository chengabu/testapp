package com.example.testapplibrary.model;

import java.util.ArrayList;
import java.util.List;

public class ConfigModel {

private int urlCount;
private boolean http;
private boolean https;
private List<String> urls = new ArrayList<String>();

public int getUrlCount() {
	return urlCount;
}

public void setUrlCount(int urlCount) {
	this.urlCount = urlCount;
}

public boolean isHttp() {
	return http;
}

public void setHttp(boolean http) {
	this.http = http;
}

public boolean isHttps() {
	return https;
}

public void setHttps(boolean https) {
	this.https = https;
}

public List<String> getUrls() {
	return urls;
}

public void setUrls(List<String> urls) {
	this.urls = urls;
}

}
