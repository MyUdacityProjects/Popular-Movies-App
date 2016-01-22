package com.example.android.popular_movies_app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author harshita.k
 */
public interface MovieService {

    @GET("3/discover/movie?sort_by={sortBy}&api_key={apiKey}")
    Call<List<Movie>> getMovies(@Query("sort_by") String sortBy, @Query("api_key") String apiKey);
}
