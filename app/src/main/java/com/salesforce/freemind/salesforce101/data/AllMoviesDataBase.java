package com.salesforce.freemind.salesforce101.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.ExecOnCreate;
import net.simonvt.schematic.annotation.OnConfigure;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by anant on 2017-09-21.
 */

@Database( version = AllMoviesDataBase.VERSION)
public class AllMoviesDataBase {

    private AllMoviesDataBase(){
    }
    public static final int  VERSION = 2 ;

    //Table structure of the all movies  table

    @Table(AllMoviesColumns.class)
    public static final String allmovies = "allmovies";

    @Table(ReviewsColumns.class)
    public static final String reviews = "reviews";

    @Table(TrailersColumns.class)
    public static final String trailers = "trailers";


    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {
    }

    @OnUpgrade
    public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
    }

    @OnConfigure
    public static void onConfigure(SQLiteDatabase db) {
    }

    @ExecOnCreate
    public static final String EXEC_ON_CREATE = "SELECT * FROM " + allmovies;









}
