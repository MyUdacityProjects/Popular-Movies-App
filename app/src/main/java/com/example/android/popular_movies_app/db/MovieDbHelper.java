package com.example.android.popular_movies_app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popular_movies_app.db.MovieContracts.*;

/**
 * @author harshita.k
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    public static final int DATABSE_VERSION = 6;
    public static final String DATABASE_NAME = "popularmovie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_MOVIE = "CREATE TABLE " + MOVIES_TABLE.TABLE_NAME + "(" +
                MOVIES_TABLE._ID + " INTEGER PRIMARY KEY," +
                MOVIES_TABLE.COLUMN_TITLE + " TEXT NOT NULL," +
                MOVIES_TABLE.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MOVIES_TABLE.COLUMN_POSTER_IMAGE + " TEXT NOT NULL," +
                MOVIES_TABLE.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                MOVIES_TABLE.COLUMN_VOTE_AVERAGE + " REAL TEXT NOT NULL" +
                ")";
        db.execSQL(CREATE_TABLE_MOVIE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MOVIES_TABLE.TABLE_NAME);
        onCreate(db);

    }

}
