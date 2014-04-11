package com.example.testapplibrary.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.text.TextUtils;
import android.util.Log;

public class ExecuteShellTask {

private final static ProcessBuilder builder;

private String[] command;

static {
	builder = new ProcessBuilder(new ArrayList<String>());
}

public ExecuteShellTask(String... command) {
	super();
	this.command = command;
}

public List<String> execute() throws Exception {
	
	String commandStr = TextUtils.join("", command);
	Log.d("ExecuteShellTask", "Start to execute command : " + commandStr);

	List<String> lines = new ArrayList<String>();

	builder.command(command);
	Process process = builder.start();
	BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	try {
		readInputStream(lines, reader);
	} catch (Exception e) {
		Log.d("ExecuteShellTask", "Failed to read the result of command : " + commandStr);
		throw e;
	}

	return lines;
}

private void readInputStream(List<String> lines, BufferedReader reader) throws IOException {
	String line = null;
	while ((line = reader.readLine()) != null) {
		lines.add(line);
	}
}

public void setCommand(String[] command) {
	this.command = command;
}



}
