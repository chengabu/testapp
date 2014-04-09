package com.example.testapplibrary.custom.handler;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class UpdateViewTextHandler extends Handler {

public static final String MSG_COLLECTING_FORMAT = "collecting memory info...\n%d of %d finished.";
public static final String MSG_FINISH_FORMAT = "Finish collecting memory info!";

private TextView view;

private String mMessage;

public UpdateViewTextHandler(TextView view, String mMessage) {
	super();
	this.view = view;
	this.mMessage = mMessage;
}

public UpdateViewTextHandler(TextView view) {
	super();
	this.view = view;
}

public void updateTextView(Message msg) {
	super.handleMessage(msg);
	int count = msg.arg1;
	int finish = msg.arg2;
	String message = "collecting memory info...";
	if (mMessage != null && !mMessage.equals("")) {
		message = mMessage;
	} else if (finish == count) {
		message = MSG_FINISH_FORMAT;
	} else {
		message = String.format(MSG_COLLECTING_FORMAT, count, finish);
	}

	if (view != null) {
		view.setText(message);
		// view.postinv
	}
}

public void setConstainMessage(String mMessage) {
	this.mMessage = mMessage;
}

}
