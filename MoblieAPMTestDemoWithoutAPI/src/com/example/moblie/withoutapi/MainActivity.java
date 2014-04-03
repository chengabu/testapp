package com.example.moblie.withoutapi;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import com.example.testapplibrary.model.ConfigModel;

public class MainActivity extends Activity {

public static final String CONFIG_NUMBER_OF_REQUEST = "Config_number";
public static final String CONFIG_IS_HTTP = "config_http";
public static final String CONFIG_IS_HTTPs = "config_https";
public static final String CONFIG_URLS = "config_urls";

private ArrayAdapter<String> mAdapter;
private List<String> listViewValues;
private boolean httpValues;

@Override
protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	setContentView(R.layout.config_activity);
	
	listViewValues = new ArrayList<String>();
	
	ListView view = (ListView) this.findViewById(R.id.urlListView);
//	String caseKey = "case";
//	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
//	String[] testCases = this.getResources().getStringArray(
//					R.array.caseNames);
//	for (String testCase : testCases) {
//		Map<String, String> dataItem = new HashMap<String, String>();
//		dataItem.put(caseKey, testCase);
//		data.add(dataItem);
//	}

	
	RadioButton rb = (RadioButton) this.findViewById(R.id.radio_http);
	boolean isHttpSelect = rb.isChecked();
	
	listViewValues = getListViewValue(isHttpSelect);
	
	mAdapter = new ArrayAdapter(this, R.layout.main_list_item, R.id.text, listViewValues);
	view.setAdapter(mAdapter);
	
	Button goBtn = (Button) this.findViewById(R.id.config_go_btn);
	
	goBtn.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View view) {
			ConfigModel config = getConfig();
			Intent intent = new Intent(MainActivity.this, HttpRequestWithoutAPI.class);
			intent.putExtra(CONFIG_NUMBER_OF_REQUEST, config.getUrlCount());
			intent.putExtra(CONFIG_IS_HTTP, config.isHttp());
			intent.putExtra(CONFIG_IS_HTTPs, config.isHttps());
			List<String> urls = config.getUrls();
			if (urls.size() > 0) {
				String[] urlArray = new String[urls.size()];
				for (int i = 0; i < urls.size(); i++) {
					urlArray[i] = urls.get(i);
				}
				intent.putExtra(CONFIG_URLS, urlArray);
			}
			startActivity(intent);
		}
	});
	
	
//	itemOnClick(view);
}

private ConfigModel getConfig() {
	ConfigModel model = new ConfigModel();
	EditText numOfUrl = (EditText) this.findViewById(R.id.config_num_of_request);
	CharSequence numStr = (CharSequence) numOfUrl.getText();
	if (numStr != null) {
		int count = Integer.parseInt(numStr.toString());
		model.setUrlCount(count);
	}
	
	RadioButton rb = (RadioButton) this.findViewById(R.id.radio_http);
	boolean isHttpSelect = rb.isChecked();
	listViewValues = getListViewValue(isHttpSelect);
	model.setHttp(isHttpSelect);
	model.setHttps(!isHttpSelect);
	model.setUrls(listViewValues);
	return model;
}

private List<String> getListViewValue(boolean isHttpSelect) {
	List<String> listViewValues = new ArrayList<String>();
	String[] urlArray = null;
	if (isHttpSelect) {
		urlArray = this.getResources().getStringArray(com.example.testapplibrary.R.array.differentDomainUrls);
		httpValues = true;
	} else {
		urlArray = this.getResources().getStringArray(com.example.testapplibrary.R.array.differentDomainHttpsUrls);
		httpValues = false;
	}
	
	for (String url : urlArray) {
		listViewValues.add(url);
	}
	return listViewValues;
}

private void itemOnClick(ListView view) {
	view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
			CharSequence caseName = getResources().getStringArray(
							R.array.caseActivityClasses)[position];
			if (caseName != null) {
				try {
					Class activityClass = Class.forName(caseName.toString());
					Intent intent = new Intent(MainActivity.this,
									activityClass);
					startActivity(intent);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	});
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
}

public void onRadioButtonClicked(View view) {
	int id = view.getId();
	switch (id) {
	case R.id.radio_http : {
		if (httpValues) {
			//DO nothing
		} else {
			listViewValues = getListViewValue(true);
			updateListViewValue();
		}
		break;
	} 
	case R.id.radio_https : {
		if (httpValues) {
			listViewValues = getListViewValue(false);
			updateListViewValue();
		} else {
			//DO nothing
		}
		break;
	}
	default : break;
	}
}

private void updateListViewValue() {
	mAdapter.clear();
	for (String url : listViewValues) {
		mAdapter.add(url);
	}
	mAdapter.notifyDataSetChanged();
}

@Override
protected void onRestoreInstanceState(Bundle savedInstanceState) {
	super.onRestoreInstanceState(savedInstanceState);
	Log.d("onRestoreInstanceState", "onRestoreInstanceState");
}

@Override
protected void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);
	Log.d("onSaveInstanceState", "onSaveInstanceState");
}


}
