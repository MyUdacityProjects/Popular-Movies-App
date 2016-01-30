package com.example.android.popular_movies_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.popular_movies_app.adapters.MovieAdapter;
import com.example.android.popular_movies_app.models.Movie;
import com.example.android.popular_movies_app.models.ListResponse;
import com.example.android.popular_movies_app.services.MovieClient;
import com.example.android.popular_movies_app.services.MovieService;
import com.example.android.popular_movies_app.utils.APIConstants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MovieAdapter mMovieAdapter;
    public ProgressDialog dialog;
    private MovieService movieService;

    @Override
    public void onPause() {
        super.onPause();
        dialog = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main_fragment, menu);
    }

    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_sort_popular:
                if (isOnline()) {
                    fetchMovies(APIConstants.SORT_POPULARITY);
                }
                return true;
            case R.id.action_sort_rating:
                if (isOnline()) {
                    fetchMovies(APIConstants.SORT_RATING);
                }
                return true;
            default:
                return true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final List<Movie> movies = new ArrayList<>();

        mMovieAdapter = new MovieAdapter(getActivity(), movies);

        GridView movieGrid = (GridView) rootView.findViewById(R.id.movie_grid);
        movieGrid.setAdapter(mMovieAdapter);
        movieService = MovieClient.createService(MovieService.class);

        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailActivity = new Intent(getActivity(), DetailActivity.class);
                detailActivity.putExtra("MOVIE", movies.get(position));
                startActivity(detailActivity);
            }
        });


        if (isOnline()) {
            fetchMovies(APIConstants.SORT_POPULARITY);
        }


        return rootView;
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            Toast.makeText(getContext(), getString(R.string.internet_conn_msg), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void fetchMovies(String sort_order) {
        Call<ListResponse<Movie>> moviesCall = movieService.getMovies(sort_order);

        moviesCall.enqueue(new Callback<ListResponse<Movie>>() {
            @Override
            public void onResponse(Response<ListResponse<Movie>> response) {
                List<Movie> movieList = response.body().getResults();
                mMovieAdapter.clear();
                for (Movie movie : movieList) {
                    mMovieAdapter.add(movie);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), "Shucks!", Toast.LENGTH_LONG).show();
            }
        });
    }


}
