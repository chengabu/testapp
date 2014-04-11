package com.example.testapplibrary.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.ContentValues;
import android.content.Context;
import android.os.Debug;
import android.text.TextUtils;

import com.example.testapplibrary.database.HttpRequstResultProvider;

public class CommonUtils {

public static ContentValues parsePsCommand(List<String> rawResults, String pid) {
	ContentValues values = new ContentValues();
	boolean start = false;
	if (rawResults != null && pid != null) {
		for (String line : rawResults) {
			if (start) {
				String[] columns = TextUtils.split(line, "\\s+");
				if (columns != null && columns[0].trim().equals(pid)) {
					values.put(HttpRequstResultProvider.COLUMN_PID, columns[0].trim());
					values.put(HttpRequstResultProvider.COLUMN_CPU, columns[2].trim());
					values.put(HttpRequstResultProvider.COLUMN_VSS, columns[5].trim());
					values.put(HttpRequstResultProvider.COLUMN_VSS, columns[5].trim());
					values.put(HttpRequstResultProvider.COLUMN_RSS, columns[6].trim());
					values.put(HttpRequstResultProvider.COLUMN_NAME, columns[9].trim());
					break;
				}
			} else {
				continue;
			}

			if (!start && line.contains("PID")) {
				start = true;
			}
		}
	}
	return values;
}

public static ContentValues buildMemoryContentValue(Context context, String currentDate, boolean withApi) {
	ActivityManager mamager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	MemoryInfo systemMemoryInfo = new MemoryInfo();
	mamager.getMemoryInfo(systemMemoryInfo);
	Debug.MemoryInfo meminfo = new Debug.MemoryInfo();
	Debug.getMemoryInfo(meminfo);

	ContentValues value = new ContentValues();
	value.put(HttpRequstResultProvider.COLUMN_WITH_API, withApi);
	value.put(HttpRequstResultProvider.COLUMN_PPS, meminfo.getTotalPss());

	value.put(HttpRequstResultProvider.COLUMN_PRIVATE_DIRTY, meminfo.getTotalPrivateDirty());
//	value.put(HttpRequstResultProvider.COLUMN_PRIVATE_CLEAN, meminfo.getTotalPrivateClean());
	value.put(HttpRequstResultProvider.COLUMN_SHARED_DIRTY, meminfo.getTotalSharedDirty());
//	value.put(HttpRequstResultProvider.COLUMN_SHARED_CLEAN, meminfo.getTotalSharedClean());
	value.put(HttpRequstResultProvider.COLUMN_TOTAL_MEMORY, systemMemoryInfo.totalMem);
	value.put(HttpRequstResultProvider.COLUMN_AVAIL_MEMORY, systemMemoryInfo.availMem);
	value.put(HttpRequstResultProvider.COLUMN_CREATED_AT, currentDate);
	value.put(HttpRequstResultProvider.COLUMN_UPDATED_AT, currentDate);
	return value;
}

}
