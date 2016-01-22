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

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MovieAdapter mMovieAdapter;
    public ProgressDialog dialog;

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
                    new FetchMovies(getActivity(), mMovieAdapter, dialog).execute(APIConstants.SORT_POPULARITY);
                }
                return true;
            case R.id.action_sort_rating:
                if (isOnline()) {
                    new FetchMovies(getActivity(), mMovieAdapter, dialog).execute(APIConstants.SORT_RATING);
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

        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailActivity = new Intent(getActivity(), DetailActivity.class);
                detailActivity.putExtra("MOVIE", movies.get(position));
                startActivity(detailActivity);
            }
        });
        if (isOnline()) {
            FetchMovies fetchMovies = new FetchMovies(getActivity(), mMovieAdapter, dialog);
            fetchMovies.execute(APIConstants.SORT_POPULARITY);
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


}
