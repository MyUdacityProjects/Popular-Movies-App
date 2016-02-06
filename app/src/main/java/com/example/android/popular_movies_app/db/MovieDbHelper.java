package com.example.android.popular_movies_app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popular_movies_app.db.MovieContracts.*;

/**
 * @author harshita.k
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    public static final int DATABSE_VERSION = 1;
    public static final String DATABSE_NAME = "popularmovie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABSE_NAME, null, DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_MOVIE = "CREATE TABLE " + FAVOURITES_TABLE.TABLE_NAME + "(" +
                FAVOURITES_TABLE._ID + " INTEGER PRIMARY KEY," +
                FAVOURITES_TABLE.COLUMN_TITLE + " TEXT NOT NULL," +
                FAVOURITES_TABLE.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                FAVOURITES_TABLE.COLUMN_POSTER_IMAGE + " TEXT NOT NULL," +
                FAVOURITES_TABLE.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                FAVOURITES_TABLE.COLUMN_POPULARITY + " REAL NOT NULL," +
                FAVOURITES_TABLE.COLUMN_VOTE_COUNT + " INTEGER TEXT NOT NULL" +
                ")";
        db.execSQL(CREATE_TABLE_MOVIE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF EXISTS  " + FAVOURITES_TABLE.TABLE_NAME);
        onCreate(db);

    }

}
