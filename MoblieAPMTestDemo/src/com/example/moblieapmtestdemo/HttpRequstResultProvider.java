package com.example.moblieapmtestdemo;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class HttpRequstResultProvider extends ContentProvider {

public final static String AUTHORITY = "com.dell.test.http";
public final static String DBNAME = "HTTPSDB";
public final static String HTTP_TABLE_NAME = "HTTP_RESULT";
public final static String RESPONE_TIME_TABLE_NAME = "RESPONSE_TIME";

public final static String COLUMN_ID = "_ID";
public final static String COLUMN_URL = "URL";
public final static String COLUMN_RESPONSE_CODE = "RESPONSE_CODE";
public final static String COLUMN_REQUEST_METHOD = "REQUEST_METHOD";
public final static String COLUMN_INCLUDE_API = "INCLUDE_API";
public final static String COLUMN_RESPONE_TIME = "RESPONE_TIME";
public final static String COLUMN_RESPONE_CONTENT = "RESPONE_CONTENT";
public final static String COLUMN_RESPONSE_TIME_ID = "RESPONSE_TIME_ID";
public final static String COLUMN_IS_HTTPS = "IS_HTTPS";

public final static String COLUMN_CREATED_AT = "CREATE_AT";
public final static String COLUMN_UPDATED_AT = "UPDATED_AT";

public final static String COLUMN_TOTAL_RESPONSE_TIME = "TOTAL_RESPONSE_TIME";
public final static String COLUMN_AVERAGE_RESPONSE_TIME = "AVERAGE_RESPONSE_TIME";
public final static String COLUMN_NUM_OF_REQUEST = "COLUMN_NUM_OF_REQUEST";

private final static String CONTENT_TYPE_REQUEST = "vnd.android.cursor.dir/vnd.com.dell.test.provider."
													+ HTTP_TABLE_NAME;
private final static String CONTENT_ITEM_TYPE_REQUEST = "vnd.android.cursor.item/vnd.com.dell.test.provider."
														+ HTTP_TABLE_NAME;
private final static String CONTENT_TYPE_RESPONSE_TIME = "vnd.android.cursor.dir/vnd.com.dell.test.provider."
															+ RESPONE_TIME_TABLE_NAME;
private final static String CONTENT_ITEM_TYPE_RESPONSE_TIME = "vnd.android.cursor.item/vnd.com.dell.test.provider."
																+ RESPONE_TIME_TABLE_NAME;

private final static int HTTP_REQUEST = 1;
private final static int HTTP_REQUEST_ID = 2;
private final static int RESPONSE_TIME = 3;
private final static int RESPONSE_TIME_ID = 4;

private final static String TAG_QUERY = "HttpRequstResultProvider.query";
private final static String TAG_INSTER = "HttpRequstResultProvider.insert";

public final static String[] RESPONSE_TIME_PROJECTION;

private final static UriMatcher mUriMatcher;

private SQLiteOpenHelper mOpenHelper;
private ContentResolver mResolver;
private final static Map<String, String> HTTP_COLUMN_MAP = new HashMap<String, String>();
private final static Map<String, String> RESPONSE_TIME_COLUMN_MAP = new HashMap<String, String>();

public static final Uri HTTP_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + HTTP_TABLE_NAME);
public static final Uri RESPONSE_TIME_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + RESPONE_TIME_TABLE_NAME);

static {
	mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	mUriMatcher.addURI(AUTHORITY, HTTP_TABLE_NAME, HTTP_REQUEST);
	mUriMatcher.addURI(AUTHORITY, HTTP_TABLE_NAME + "/#", HTTP_REQUEST_ID);

	mUriMatcher.addURI(AUTHORITY, RESPONE_TIME_TABLE_NAME, RESPONSE_TIME);
	mUriMatcher.addURI(AUTHORITY, RESPONE_TIME_TABLE_NAME + "/#", RESPONSE_TIME_ID);

	HTTP_COLUMN_MAP.put(COLUMN_ID, COLUMN_ID);
	HTTP_COLUMN_MAP.put(COLUMN_URL, COLUMN_URL);
	HTTP_COLUMN_MAP.put(COLUMN_RESPONSE_CODE, COLUMN_RESPONSE_CODE);
	HTTP_COLUMN_MAP.put(COLUMN_REQUEST_METHOD, COLUMN_REQUEST_METHOD);
	HTTP_COLUMN_MAP.put(COLUMN_INCLUDE_API, COLUMN_INCLUDE_API);
	HTTP_COLUMN_MAP.put(COLUMN_RESPONE_TIME, COLUMN_RESPONE_TIME);
	HTTP_COLUMN_MAP.put(COLUMN_RESPONE_CONTENT, COLUMN_RESPONE_CONTENT);
	HTTP_COLUMN_MAP.put(COLUMN_RESPONSE_TIME_ID, COLUMN_RESPONSE_TIME_ID);
	HTTP_COLUMN_MAP.put(COLUMN_CREATED_AT, COLUMN_CREATED_AT);
	HTTP_COLUMN_MAP.put(COLUMN_UPDATED_AT, COLUMN_UPDATED_AT);

	RESPONSE_TIME_COLUMN_MAP.put(COLUMN_ID, COLUMN_ID);
	RESPONSE_TIME_COLUMN_MAP.put(COLUMN_TOTAL_RESPONSE_TIME, COLUMN_TOTAL_RESPONSE_TIME);
	RESPONSE_TIME_COLUMN_MAP.put(COLUMN_AVERAGE_RESPONSE_TIME, COLUMN_AVERAGE_RESPONSE_TIME);
	RESPONSE_TIME_COLUMN_MAP.put(COLUMN_NUM_OF_REQUEST, COLUMN_NUM_OF_REQUEST);
	RESPONSE_TIME_COLUMN_MAP.put(COLUMN_CREATED_AT, COLUMN_CREATED_AT);
	RESPONSE_TIME_COLUMN_MAP.put(COLUMN_UPDATED_AT, COLUMN_UPDATED_AT);

	RESPONSE_TIME_PROJECTION = new String[] { COLUMN_ID, COLUMN_TOTAL_RESPONSE_TIME, COLUMN_AVERAGE_RESPONSE_TIME,
												COLUMN_NUM_OF_REQUEST, COLUMN_CREATED_AT, COLUMN_UPDATED_AT };
}

@Override
public boolean onCreate() {
	// mdb = new SQLiteDatabase
	mOpenHelper = new HttpDatabaseHelper(getContext(), null, 1);
	mResolver = getContext().getContentResolver();
	return true;
}

@Override
public Cursor query(Uri uri, String[] projection, String selection,
				String[] selectionArgs, String sortOrder) {
	SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
	String tabelName = null;
	int code = mUriMatcher.match(uri);
	String id = null;
	switch (code) {
	case HTTP_REQUEST:
		break;
	case HTTP_REQUEST_ID:
		id = uri.getPathSegments().get(1);
		break;
	case RESPONSE_TIME:
		tabelName = RESPONE_TIME_TABLE_NAME;
		break;
	case RESPONSE_TIME_ID: {
		tabelName = RESPONE_TIME_TABLE_NAME;
		id = uri.getPathSegments().get(1);
		break;
	}
	default:
		break;
	}

	Cursor cursor = null;
	if (tabelName != null) {
		sqlBuilder.setTables(tabelName);
		Log.d(TAG_QUERY, "query table : " + tabelName);
//		if (HTTP_TABLE_NAME.equals(tabelName)) {
//			sqlBuilder.setProjectionMap(HTTP_COLUMN_MAP);
//		} else if (RESPONE_TIME_TABLE_NAME.equals(tabelName)) {
//			sqlBuilder.setProjectionMap(RESPONSE_TIME_COLUMN_MAP);
//		}

		if (id != null) {
			Log.d(TAG_QUERY, "query _id : " + id);
			sqlBuilder.appendWhere(COLUMN_ID + "=" + id);
		}
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, null);
		cursor.setNotificationUri(mResolver, uri);
	}
	return cursor;
}

@Override
public String getType(Uri uri) {
	int code = mUriMatcher.match(uri);
	switch (code) {
	case HTTP_REQUEST:
		return CONTENT_TYPE_REQUEST;
	case HTTP_REQUEST_ID:
		return CONTENT_ITEM_TYPE_REQUEST;
	case RESPONSE_TIME:
		return CONTENT_TYPE_RESPONSE_TIME;
	case RESPONSE_TIME_ID:
		return CONTENT_ITEM_TYPE_RESPONSE_TIME;
	default:
		return null;
	}
}

@Override
public Uri insert(Uri uri, ContentValues values) {
	SQLiteDatabase db = mOpenHelper.getWritableDatabase();
	int code = mUriMatcher.match(uri);
	String tableName = null;

	switch (code) {
	case HTTP_REQUEST:
		tableName = HTTP_TABLE_NAME;
		break;
	case RESPONSE_TIME:
		tableName = RESPONE_TIME_TABLE_NAME;
		break;
	default:
		break;
	}

	Uri newUri = null;
	if (tableName != null) {
		Log.d(TAG_INSTER, "Insert table : " + tableName);
		long rowId = db.insert(tableName, null, values);
		newUri = ContentUris.withAppendedId(uri, rowId);
		mResolver.notifyChange(newUri, null);
	}

	return newUri;
}

@Override
public int delete(Uri uri, String selection, String[] selectionArgs) {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public int update(Uri uri, ContentValues values, String selection,
				String[] selectionArgs) {
	// TODO Auto-generated method stub
	return 0;
}
}

class HttpDatabaseHelper extends SQLiteOpenHelper {

public HttpDatabaseHelper(Context context, CursorFactory factory, int version,
							DatabaseErrorHandler errorHandler) {
	super(context, HttpRequstResultProvider.DBNAME, factory, version, errorHandler);
}

public HttpDatabaseHelper(Context context, CursorFactory factory, int version) {
	super(context, HttpRequstResultProvider.DBNAME, factory, version);
}

private static final String SQL_CREATE_HTTP_TABLE = "CREATE TABLE " + HttpRequstResultProvider.HTTP_TABLE_NAME +
													" " +
													"(" +
													HttpRequstResultProvider.COLUMN_ID
													+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
													HttpRequstResultProvider.COLUMN_URL + " TEXT, " +
													HttpRequstResultProvider.COLUMN_RESPONSE_CODE + " INTEGER, " +
													HttpRequstResultProvider.COLUMN_REQUEST_METHOD + " TEXT," +
													HttpRequstResultProvider.COLUMN_INCLUDE_API + " INTEGER, " +
													HttpRequstResultProvider.COLUMN_RESPONE_TIME + " INTEGER, " +
													HttpRequstResultProvider.COLUMN_RESPONSE_TIME_ID + " TEXT, " +
													HttpRequstResultProvider.COLUMN_RESPONE_CONTENT + " TEXT, " +
													HttpRequstResultProvider.COLUMN_IS_HTTPS + " INTEGER, " +
													HttpRequstResultProvider.COLUMN_CREATED_AT + " TEXT, " +
													HttpRequstResultProvider.COLUMN_UPDATED_AT + " TEXT " +
													");";

private static final String SQL_CREATE_RESPONSE_TIME_TABLE = "CREATE TABLE "
																+ HttpRequstResultProvider.RESPONE_TIME_TABLE_NAME +
																" " +
																"(" +
																HttpRequstResultProvider.COLUMN_ID
																+ " TEXT PRIMARY KEY, " +
																HttpRequstResultProvider.COLUMN_TOTAL_RESPONSE_TIME
																+ " INTEGER, " +
																HttpRequstResultProvider.COLUMN_AVERAGE_RESPONSE_TIME
																+ " REAL, " +
																HttpRequstResultProvider.COLUMN_NUM_OF_REQUEST
																+ " INTEGER, " +
																HttpRequstResultProvider.COLUMN_CREATED_AT + " TEXT, " +
																HttpRequstResultProvider.COLUMN_UPDATED_AT + " TEXT " +
																");";

@Override
public void onCreate(SQLiteDatabase db) {
	db.execSQL(SQL_CREATE_HTTP_TABLE);
	db.execSQL(SQL_CREATE_RESPONSE_TIME_TABLE);

}

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	// TODO Auto-generated method stub
}

}
