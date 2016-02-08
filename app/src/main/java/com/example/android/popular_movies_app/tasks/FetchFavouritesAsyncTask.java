package com.example.android.popular_movies_app.tasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.android.popular_movies_app.adapters.MovieAdapter;
import com.example.android.popular_movies_app.db.MovieContracts;
import com.example.android.popular_movies_app.models.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * @author harshita.k
 */
public class FetchFavouritesAsyncTask extends AsyncTask<Void, Void, List<Movie>> {

    private static final String[] MOVIE_COLUMNS = {
            MovieContracts.MOVIES_TABLE._ID,
            MovieContracts.MOVIES_TABLE.COLUMN_TITLE,
            MovieContracts.MOVIES_TABLE.COLUMN_OVERVIEW,
            MovieContracts.MOVIES_TABLE.COLUMN_POSTER_IMAGE,
            MovieContracts.MOVIES_TABLE.COLUMN_VOTE_AVERAGE,
            MovieContracts.MOVIES_TABLE.COLUMN_RELEASE_DATE
    };

    private MovieAdapter mMovieAdapter;

    public static final int COL_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_OVERVIEW = 2;
    public static final int COL_IMAGE = 3;
    public static final int COL_VOTE = 4;
    public static final int COL_DATE = 5;

    private Context mContext;
    private List<Movie> mMovies;

    public FetchFavouritesAsyncTask(Context context, MovieAdapter movieAdapter, List<Movie> movies) {
        mContext = context;
        mMovieAdapter = movieAdapter;
        mMovies = movies;
    }

    private List<Movie> getFavoriteMoviesDataFromCursor(Cursor cursor) {
        List<Movie> results = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor.getString(COL_ID),
                        cursor.getString(COL_TITLE),
                        cursor.getString(COL_OVERVIEW),
                        cursor.getString(COL_IMAGE),
                        cursor.getFloat(COL_VOTE),
                        cursor.getString(COL_DATE));
                results.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return results;
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {
        Cursor cursor = mContext.getContentResolver().query(
                MovieContracts.MOVIES_TABLE.CONTENT_URI,
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
        return getFavoriteMoviesDataFromCursor(cursor);
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (movies != null) {
            if (mMovieAdapter != null) {
                mMovieAdapter.clear();
                mMovies = new ArrayList<>();
                mMovies.addAll(movies);
                for (Movie movie : movies) {
                    mMovieAdapter.add(movie);
                }
            }
        }
    }
}