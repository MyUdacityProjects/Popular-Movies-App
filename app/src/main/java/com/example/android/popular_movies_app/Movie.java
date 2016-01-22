package com.example.android.popular_movies_app;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by harshita.k on 13/12/15.
 */
public class Movie implements Parcelable {
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

    public Movie(Parcel in) {
        movieId = in.readString();
        imageURL = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
    }

    public Movie(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(imageURL);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(voteAverage);
        dest.writeString(releaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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
        return APIConstants.IMAGE_BASE_URL + APIConstants.IMAGE_SMALL_SIZE + getImageURL();
    }

    public String getRating() {
        return getVoteAverage() + "/" + APIConstants.RATING_MAX;
    }

    public String getMovieReleaseDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-mm");
        String date = "";
        try {
            Date newDate = format.parse(getReleaseDate());
            format = new SimpleDateFormat("MMM dd, yyyy");
            date = format.format(newDate);
        } catch (ParseException e) {

        }
        return date;
    }

}
