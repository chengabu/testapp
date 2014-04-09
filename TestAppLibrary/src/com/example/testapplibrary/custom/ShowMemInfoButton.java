package com.example.testapplibrary.custom;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Debug;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class ShowMemInfoButton extends Button {

public ShowMemInfoButton(Context context) {
	super(context);
	addShowMemInfoAction();
}

public ShowMemInfoButton(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	addShowMemInfoAction();
}

public ShowMemInfoButton(Context context, AttributeSet attrs) {
	super(context, attrs);
	addShowMemInfoAction();
}

public void addShowMemInfoAction() {
	this.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			ActivityManager mamager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
			MemoryInfo systemMemoryInfo = new MemoryInfo();
			mamager.getMemoryInfo(systemMemoryInfo);
			Debug.MemoryInfo meminfo = new Debug.MemoryInfo();
			Debug.getMemoryInfo(meminfo);
			StringBuffer buffer = new StringBuffer();
			buffer.append("PPS : ").append(meminfo.getTotalPss()).append("\n").append("Private dirty : ")
							.append(meminfo.getTotalPrivateDirty()).append("\n").append("Avail memory : ")
							.append(systemMemoryInfo.availMem).append("\n").append("Total memory : ")
							.append(systemMemoryInfo.totalMem);
			final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle("Memory Consumption");
			builder.setMessage(buffer.toString());
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	});
}

}
