package com.example.android.popular_movies_app.db;

import android.content.ContentValues;

import com.example.android.popular_movies_app.models.Movie;

/**
 * @author harshita.k
 */
public final class DbUtils {
    public static final ContentValues toContentValue(Movie movie) {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContracts.FAVOURITES_TABLE._ID, movie.getId());
        movieValues.put(MovieContracts.FAVOURITES_TABLE.COLUMN_TITLE, movie.getOriginalTitle());
        movieValues.put(MovieContracts.FAVOURITES_TABLE.COLUMN_OVERVIEW, movie.getOverview());
        movieValues.put(MovieContracts.FAVOURITES_TABLE.COLUMN_POSTER_IMAGE, movie.getBackdropPath());
        movieValues.put(MovieContracts.FAVOURITES_TABLE.COLUMN_RELEASE_DATE, movie.getRelease_date());
        movieValues.put(MovieContracts.FAVOURITES_TABLE.COLUMN_POPULARITY, movie.getPopularity());
        movieValues.put(MovieContracts.FAVOURITES_TABLE.COLUMN_VOTE_COUNT, movie.getVoteCount());
        return movieValues;
    }
}
