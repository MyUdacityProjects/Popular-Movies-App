package com.example.android.popular_movies_app.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.android.popular_movies_app.db.MovieContracts.FAVOURITE_TABLE;
import com.example.android.popular_movies_app.db.MovieContracts.MOVIES_TABLE;

/**
 * @author harshita.k
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private MovieDbHelper dbHelper;

    static final int MOVIE = 100;
    static final int MOVIE_WITH_ID = 101;
    static final int FAV = 102;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContracts.CONTENT_AUTHORITY;
        matcher.addURI(authority, MOVIES_TABLE.TABLE_NAME, MOVIE);
        matcher.addURI(authority, MOVIES_TABLE.TABLE_NAME + "/#", MOVIE_WITH_ID);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        dbHelper = new MovieDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);
        String type = "";
        switch (match) {
            case MOVIE:
                type = MOVIES_TABLE.CONTENT_TYPE;
                break;
            case MOVIE_WITH_ID:
                type = MOVIES_TABLE.CONTENT_ITEM_TYPE;
                break;
            case FAV:
                type = FAVOURITE_TABLE.CONTENT_TYPE;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return type;
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;
        int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                retCursor = dbHelper.getReadableDatabase().query(
                        MOVIES_TABLE.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_WITH_ID:
                String movieIdSelection = MOVIES_TABLE.TABLE_NAME + "." + MOVIES_TABLE._ID + "= ?";
                String[] movieSelectionArgs = new String[]{MOVIES_TABLE.getMovieIDFromUri(uri)};
                retCursor = dbHelper.getReadableDatabase().query(
                        MOVIES_TABLE.TABLE_NAME,
                        projection,
                        movieIdSelection,
                        movieSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException();

        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri returnUri;
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (match) {
            case MOVIE:
                long movieId = db.insert(MOVIES_TABLE.TABLE_NAME, null, values);
                if (movieId != -1) {
                    returnUri = MOVIES_TABLE.buildMovieUri(movieId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (selection == null) {
            selection = "1";
        }
        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(MOVIES_TABLE.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        db.close();
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated;
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(MOVIES_TABLE.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        db.close();
        return rowsUpdated;
    }

}
