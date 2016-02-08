package com.example.android.popular_movies_app.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.android.popular_movies_app.db.MovieContracts;
import com.example.android.popular_movies_app.db.MovieDbHelper;

import java.util.HashSet;

/**
 * @author harshita.k
 */
public class TestDb extends AndroidTestCase {
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Exception {
        SQLiteDatabase db = new MovieDbHelper(
                this.mContext).getWritableDatabase();

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieContracts.MOVIES_TABLE.TABLE_NAME);

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieContracts.MOVIES_TABLE.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(MovieContracts.MOVIES_TABLE._ID);
        locationColumnHashSet.add(MovieContracts.MOVIES_TABLE.COLUMN_OVERVIEW);
        locationColumnHashSet.add(MovieContracts.MOVIES_TABLE.COLUMN_POPULARITY);
        locationColumnHashSet.add(MovieContracts.MOVIES_TABLE.COLUMN_POSTER_IMAGE);
        locationColumnHashSet.add(MovieContracts.MOVIES_TABLE.COLUMN_RELEASE_DATE);
        locationColumnHashSet.add(MovieContracts.MOVIES_TABLE.COLUMN_TITLE);
        locationColumnHashSet.add(MovieContracts.MOVIES_TABLE.COLUMN_VOTE_AVERAGE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required movie
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required movie entry columns",
                locationColumnHashSet.isEmpty());
        db.close();
    }

    public void testInsertToMovieTable() {
        SQLiteDatabase db = new MovieDbHelper(
                this.mContext).getWritableDatabase();
        ContentValues movieContentValue = DbUtils.createMovieData();
        long movieId;
        movieId = db.insert(MovieContracts.MOVIES_TABLE.TABLE_NAME, null, movieContentValue);
        assertTrue("Error : Failed to insert the movie", movieId != -1);

        Cursor cursor = db.query(MovieContracts.MOVIES_TABLE.TABLE_NAME, null, null, null, null, null, null);

        assertTrue("Error: No Records returned from movie query", cursor.moveToFirst());

        DbUtils.validateCurrentRecord("Error: movie Query Validation Failed",
                cursor, movieContentValue);

        assertFalse("Error: More than one record returned from movie query",
                cursor.moveToNext());

        cursor.close();
        db.close();

    }
}
