package com.example.android.popular_movies_app.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.example.android.popular_movies_app.db.MovieContracts;

import java.util.Map;
import java.util.Set;

/**
 * @author harshita.k
 */
public class DbUtils extends AndroidTestCase {

    static ContentValues createMovieData() {
        ContentValues movieContentValue = new ContentValues();
        movieContentValue.put(MovieContracts.MOVIES_TABLE._ID, 102899);
        movieContentValue.put(MovieContracts.MOVIES_TABLE.COLUMN_TITLE, "Ant-Man");
        movieContentValue.put(MovieContracts.MOVIES_TABLE.COLUMN_OVERVIEW, "Armed with the astonishing ability to shrink in scale but increase in strength, con-man Scott Lang must embrace his inner-hero and help his mentor, Dr. Hank Pym, protect the secret behind his spectacular Ant-Man suit from a new generation of towering threats. Against seemingly insurmountable obstacles, Pym and Lang must plan and pull off a heist that will save the world.");
        movieContentValue.put(MovieContracts.MOVIES_TABLE.COLUMN_POSTER_IMAGE, "/D6e8RJf2qUstnfkTslTXNTUAlT.jpg");
        movieContentValue.put(MovieContracts.MOVIES_TABLE.COLUMN_POPULARITY, 11.68325);
        movieContentValue.put(MovieContracts.MOVIES_TABLE.COLUMN_VOTE_AVERAGE, 2519);
        movieContentValue.put(MovieContracts.MOVIES_TABLE.COLUMN_RELEASE_DATE, "2015-07-14");
        return movieContentValue;
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

}
