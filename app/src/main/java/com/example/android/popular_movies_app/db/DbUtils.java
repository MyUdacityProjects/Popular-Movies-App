package com.example.android.popular_movies_app.db;

import android.content.ContentValues;

import com.example.android.popular_movies_app.models.Movie;

/**
 * @author harshita.k
 */
public final class DbUtils {
    public static final ContentValues toContentValue(Movie movie) {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContracts.MOVIES_TABLE._ID, movie.getId());
        movieValues.put(MovieContracts.MOVIES_TABLE.COLUMN_TITLE, movie.getOriginalTitle());
        movieValues.put(MovieContracts.MOVIES_TABLE.COLUMN_OVERVIEW, movie.getOverview());
        movieValues.put(MovieContracts.MOVIES_TABLE.COLUMN_POSTER_IMAGE, movie.getPosterPath());
        movieValues.put(MovieContracts.MOVIES_TABLE.COLUMN_RELEASE_DATE, movie.getRelease_date());
        movieValues.put(MovieContracts.MOVIES_TABLE.COLUMN_POPULARITY, movie.getPopularity());
        movieValues.put(MovieContracts.MOVIES_TABLE.COLUMN_VOTE_COUNT, movie.getVoteCount());
        return movieValues;
    }
}
