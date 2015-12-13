package com.example.android.popular_movies_app;

/**
 * Created by harshita.k on 13/12/15.
 */
public class Movie {
    int movieId;
    String imageURL;
    final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    final static String IMAGE_SIZE = "w185";

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getImageFullURL() {
        return IMAGE_BASE_URL + IMAGE_SIZE+ imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Movie(int movieId) {
        this.movieId = movieId;
    }

    public Movie(String imageURL) {
        this.imageURL = imageURL;
    }
}
