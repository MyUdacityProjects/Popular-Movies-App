package com.example.android.popular_movies_app.services;

import com.example.android.popular_movies_app.models.ListResponse;
import com.example.android.popular_movies_app.models.Movie;
import com.example.android.popular_movies_app.models.Review;
import com.example.android.popular_movies_app.models.TrailersList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author harshita.k
 */
public interface MovieService {

    @GET("3/discover/movie?")
    Call<ListResponse<Movie>> getMovies(@Query("sort_by") String sortBy);

    @GET("3/movie/{id}/reviews?")
    Call<ListResponse<Review>> getMovieReviews(@Path("id") String id);

    @GET("3/movie/{id}/videos?")
    Call<TrailersList> getMovieTrailers(@Path("id") String id);
}
