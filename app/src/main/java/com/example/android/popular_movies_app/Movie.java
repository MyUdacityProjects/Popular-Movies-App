package com.example.android.popular_movies_app;

/**
 * Created by harshita.k on 13/12/15.
 */
public class Movie {
    final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    final static String IMAGE_SMALL_SIZE = "w185";
    String movieId;
    String imageURL;
    String originalTitle;
    String overview;
    String voteAverage;
    String releaseDate;

    public Movie(String movieId, String imageURL) {
        this.movieId = movieId;
        this.imageURL = imageURL;
    }

    public Movie(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageFullURL() {
        return IMAGE_BASE_URL + IMAGE_SMALL_SIZE + imageURL;
    }

}
