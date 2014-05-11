package com.muzamilpeer.raspberrypicar.dblayer;

import android.database.sqlite.SQLiteDatabase;

public interface QueryExecutor {

    public void run(SQLiteDatabase database);
}
