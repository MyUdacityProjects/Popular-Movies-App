package com.example.android.popular_movies_app.utils;

/**
 * APIConstants.java
 * Purpose: Store all the constants used in the project related to THE MOVIE DB API
 *
 * @author Harshita Kasera
 */
public final class APIConstants {
    public static final String BASE_URL = "http://api.themoviedb.org/";
    public static final String OPEN_DB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    public static final String SORT_QUERY_PARAM = "sort_by";
    public static final String APPKEY_QUERY_PARAM = "api_key";
    public static final String RESULTS_KEY = "results";
    public static final String POSTER_PATH = "poster_path";
    public static final String MOVIE_ID = "id";
    public static final String MOVIE_TITLE = "original_title";
    public static final String MOVIE_OVERVIEW = "overview";
    public static final String MOVIE_RATING = "vote_average";
    public static final String MOVIE_RELEASE_DATE = "release_date";
    public static final String SORT_POPULARITY = "popularity.desc";
    public static final String SORT_RATING = "vote_average.desc";
    public final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public final static String IMAGE_SMALL_SIZE = "w185";
    public final static String RATING_MAX = "10";
    public final static int MAX_CHAR_DISPLAY = 120;
}
