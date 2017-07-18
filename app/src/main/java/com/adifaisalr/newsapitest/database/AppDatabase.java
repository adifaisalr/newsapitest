package com.adifaisalr.newsapitest.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Adi Faisal Rahman on 7/17/2017.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "NewsDatabase";
    public static final int VERSION = 1;
}
