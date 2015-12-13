package com.example.android.popular_movies_app;

/**
 * Created by harshita.k on 13/12/15.
 */
public class Movie {
    int movieId;
    String imageURL;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getImageURL() {
        return imageURL;
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
