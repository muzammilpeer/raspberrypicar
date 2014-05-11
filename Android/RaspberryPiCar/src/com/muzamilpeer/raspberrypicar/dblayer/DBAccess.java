package com.muzamilpeer.raspberrypicar.dblayer;

import java.util.ArrayList;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import com.muzamilpeer.raspberrypicar.RaspberryPiApplication;
import com.muzamilpeer.raspberrypicar.app.common.CommonUtils;

public class DBAccess {

	private SQLiteDatabase mDatabase;
	private Context mContext;
	private String responseType = "";

	public DBAccess(SQLiteDatabase database, Context context) {
		mDatabase = database;
		mContext = context;
		responseType = RaspberryPiApplication.appLanguage
				.equalsIgnoreCase("en") ? Table.COLUMN_RESPONSE_EN
				: Table.COLUMN_RESPONSE_AR;
	}

	private String generateDBTableName(Object model) {
		return model.getClass().getSimpleName().substring(2).toLowerCase();
	}

	private ContentValues generateContentValues(Object model) {
		ContentValues values = new ContentValues();
		Object[] bindkeys = CommonUtils.reflectionKeys(model);
		Object[] bindvalues = CommonUtils.reflectionValues(model);

		for (int i = 0; i < bindkeys.length; i++) {
			Log.e("KeyValue", "key=" + bindkeys[i] + ", value = "
					+ bindvalues[i] + "");
			values.put((String) bindkeys[i], (String) bindvalues[i]);
		}

		return values;
	}

	private Object parseObject(Cursor c, Object model)	 {
		Object[] bindkeys = CommonUtils.reflectionKeys(model);
		for (int i = 0; i < bindkeys.length; i++) {
			Object key = bindkeys[i];
			String value = c.getString(c.getColumnIndex(key.toString()));
			value = value == null ? "": value;
			CommonUtils.refectionSetValue(model, value, key.toString());
		}
		return model;
	}
	public ArrayList<Object> selectAll(Map<String, String> whereCriteria,
			Object model) {
		String whereClause = " where ";
		String[] whereValues = whereCriteria.values().toArray(new String[0]);

		for (Map.Entry<?, ?> entry : whereCriteria.entrySet()) {
			whereClause += " " + (String) entry.getKey() + " =  ? and ";
		}
		whereClause = whereCriteria.entrySet().size() < 1 ? "" : whereClause
				.substring(0, whereClause.length() - 5);

		String query = "SELECT * FROM " + generateDBTableName(model)
				+ whereClause;

		Cursor c = mDatabase.rawQuery(query, null);

		if (c != null) {
			ArrayList<Object> favorite_list = new ArrayList<Object>();
			c.moveToFirst();

			while (!c.isAfterLast()) {
				favorite_list.add(parseObject(c,model));
				c.moveToNext();
			}
			return favorite_list;

		}
		return null;
	}	
	public int getCount(Object model) {
		String query = "SELECT count(*) FROM " + generateDBTableName(model);

		Cursor cursor = mDatabase.rawQuery(query, null);
		int count = -1;
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				count = cursor.getInt(0);
			}
		}
		return count;
	}

	public long insert(Object model) {
		long i = mDatabase.insert(generateDBTableName(model), null,
				generateContentValues(model));
		Log.e("Rows Returned", i + "");
		return i;
	}

	public int update(Map<String, String> whereCriteria, Object model) {
		// Update Query
		String whereClause = "";
		// It's JDK 1.5 supported code
		String[] whereValues = whereCriteria.values().toArray(new String[0]);
		// It's JDK 1.6 Code below
		// String[] whereValues =
		// Arrays.copyOf(whereCriteria.values().toArray(),
		// whereCriteria.values().toArray().length, String[].class);

		for (Map.Entry<?, ?> entry : whereCriteria.entrySet()) {
			whereClause += " " + (String) entry.getKey() + " =  ? and ";
		}
		whereClause = whereCriteria.entrySet().size() < 1 ? "" : whereClause
				.substring(0, whereClause.length() - 5);

		// ContentValues values = new ContentValues();
		// values.put(responseType, response);

		int i = mDatabase.update(generateDBTableName(model),
				generateContentValues(model), whereClause, whereValues);

		Log.e("DBACCESS", i + " rows affected in " + generateDBTableName(model));
		return i;
	}

	public int getCountForUpdate(Object model, String whereClause,
			String[] whereValues) {
		String query = "SELECT count(*) FROM " + generateDBTableName(model)
				+ whereClause;

		Cursor cursor = mDatabase.rawQuery(query, whereValues);
		int count = 0;
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				count = cursor.getInt(0);
			}
		}
		return count;
	}

	public double insertorUpdate(Map<String, String> whereCriteria, Object model) {
		// Update Query
		String whereClause = " where ";
		String[] whereValues = whereCriteria.values().toArray(new String[0]);

		for (Map.Entry<?, ?> entry : whereCriteria.entrySet()) {
			whereClause += " " + (String) entry.getKey() + " =  ? and ";
		}
		whereClause = whereCriteria.entrySet().size() < 1 ? "" : whereClause
				.substring(0, whereClause.length() - 5);

		int recordFound = getCountForUpdate(model, whereClause, whereValues);
		double rowsReturned;
		if (recordFound == 0) {
			// New record Insert it
			// CommonUtils.refectionSetValue(model, response, responseType);
			rowsReturned = insert(model);
		} else {
			// CommonUtils.refectionSetValue(model, response, responseType);
			rowsReturned = update(whereCriteria, model);
		}
		Log.e("DBACCESS", rowsReturned + " rows affected in "
				+ generateDBTableName(model));
		return rowsReturned;
	}

	public void deleteAll(Object model) {
		// Delete all rows
		mDatabase.delete(generateDBTableName(model), null, null);
		Log.e("DBACCESS", "Delete All called for " + generateDBTableName(model));
	}

	public void delete(Map<String, String> whereCriteria, Object model) {
		// Delete Query
		String whereClause = "";
		// It's JDK 1.5 supported code
		String[] whereValues = whereCriteria.values().toArray(new String[0]);

		for (Map.Entry<?, ?> entry : whereCriteria.entrySet()) {
			whereClause += " " + (String) entry.getKey() + " =  ? and ";
		}
		whereClause = whereCriteria.entrySet().size() < 1 ? "" : whereClause
				.substring(0, whereClause.length() - 5);

		int i = mDatabase.delete(generateDBTableName(model), whereClause,
				whereValues);
		Log.e("DBACCESS", i + " rows affected in " + generateDBTableName(model));
	}

	public Object select(Map<String, String> whereCriteria, Object model) {
		String whereClause = " where ";
		String[] whereValues = whereCriteria.values().toArray(new String[0]);

		for (Map.Entry<?, ?> entry : whereCriteria.entrySet()) {
			whereClause += " " + (String) entry.getKey() + " =  ? and ";
		}
		whereClause = whereCriteria.entrySet().size() < 1 ? "" : whereClause
				.substring(0, whereClause.length() - 5);

		String query = "SELECT " + responseType + " FROM "
				+ generateDBTableName(model) + whereClause;

		Cursor cursor = mDatabase.rawQuery(query, whereValues);
		String response = null;
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				response = cursor.getString(0);
			}
		}
		return response;
	}



	// overhead method for news
	public Object selectNews(Map<String, String> whereCriteria, Object model) {
		String whereClause = " where ";
		String[] whereValues = whereCriteria.values().toArray(new String[0]);

		for (Map.Entry<?, ?> entry : whereCriteria.entrySet()) {
			whereClause += " " + (String) entry.getKey() + " =  ? and ";
		}
		whereClause = whereClause.substring(0, whereClause.length() - 5);

		String query = "SELECT response FROM " + generateDBTableName(model)
				+ whereClause;

		Cursor cursor = mDatabase.rawQuery(query, whereValues);
		String response = null;
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				response = cursor.getString(0);
			}
		}
		return response;
	}

}
