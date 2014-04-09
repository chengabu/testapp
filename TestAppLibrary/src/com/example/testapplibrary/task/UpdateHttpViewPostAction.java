package com.example.testapplibrary.task;

import java.util.List;

import com.example.testapplibrary.custom.handler.UpdateViewTextHandler;
import com.example.testapplibrary.model.HttpRequestModel;

public class UpdateHttpViewPostAction implements PostActionInterface {

public static final String MSG_FINISH_HTTP_REQUEST = "Finish sending http/https request.\n";

private UpdateViewTextHandler handler;

public UpdateHttpViewPostAction(UpdateViewTextHandler handler) {
	super();
	this.handler = handler;
}

@Override
public void execute(List<HttpRequestModel> models) {
	StringBuffer buffer = new StringBuffer();
	buffer.append(MSG_FINISH_HTTP_REQUEST);
	for (HttpRequestModel model : models) {
		buffer.append(model.getRequestUrl()).append(" - ").append(model.getResponseCode()).append("\n");
	}
	handler.setConstainMessage(buffer.toString());
	handler.post(new Runnable() {
		
		@Override
		public void run() {
			handler.updateTextView(handler.obtainMessage());
		}
	});
}

}
