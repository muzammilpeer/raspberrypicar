package com.muzamilpeer.raspberrypicar.dblayer;


import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.muzamilpeer.raspberrypicar.app.common.MyLog;

public class DatabaseManager {

    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static DatabaseManager instance;
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    private DatabaseManager(DatabaseHelper helper) {
        this.mDatabaseHelper = helper;
        
    }

    public static synchronized void initializeInstance(DatabaseHelper helper) {
        if (instance == null) {
            instance = new DatabaseManager(helper);
        	try {
        		helper.createDataBase();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			Log.e("Exception", e.getMessage());
    		}

        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return instance;
    }

    private synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.openDataBase();
        }
        MyLog.d("Database open counter: ","" + mOpenCounter.get());
        return mDatabase;
    }

    private synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();

        }
        MyLog.d("Database open counter: ","" + mOpenCounter.get());
    }

    public void executeQuery(QueryExecutor executor) {
        SQLiteDatabase database = openDatabase();
        executor.run(database);
        closeDatabase();
    }

    public void executeQueryTask(final QueryExecutor executor) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase database = openDatabase();
                executor.run(database);
                closeDatabase();
            }
        }).start();
    }
}
