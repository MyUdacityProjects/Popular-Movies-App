package com.example.android.popular_movies_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MovieAdapter mMovieAdapter;
    public static String SORT_POPULARITY = "popularity.desc";
    public static String SORT_RATING = "vote_average.desc";


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
        String sort_order;
        switch (item.getItemId()) {
            case R.id.action_sort_popular:
                if (isOnline()) {
                    new FetchMovies().execute(SORT_POPULARITY);
                }
                return true;
            case R.id.action_sort_rating:
                if (isOnline()) {
                    new FetchMovies().execute(SORT_RATING);
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
            FetchMovies fetchMovies = new FetchMovies();
            fetchMovies.execute(SORT_POPULARITY);
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

    public class FetchMovies extends AsyncTask<String, Void, List<Movie>> {
        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage(getString(R.string.loading_msg));
            this.dialog.show();
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            String responseJSONStr = null;
            List<Movie> moviesList = new ArrayList<>();
            String sort_order = params[0];

            try {

                final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_QUERY_PARAM = "sort_by";
                final String APPKEY_QUERY_PARAM = "api_key";

                Uri uri = Uri.parse(BASE_URL).buildUpon().
                        appendQueryParameter(SORT_QUERY_PARAM, sort_order).
                        appendQueryParameter(APPKEY_QUERY_PARAM, BuildConfig.THE_MOVIEDB_API_KEY).build();

                String requestURL = uri.toString();
                URL url = new URL(requestURL);

                // Create the request to THE MOVIE DB, and open the connection
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return moviesList;
                }
                responseJSONStr = buffer.toString();


            } catch (Exception e) {
                return moviesList;

            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
                        return moviesList;
                    }
                }
                try {
                    moviesList = getMovieDataFromJson(responseJSONStr);
                } catch (Exception e) {
                    return moviesList;
                }

            }

            return moviesList;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            mMovieAdapter.clear();
            for (Movie movie : movies) {
                mMovieAdapter.add(movie);
            }
        }

        private List<Movie> getMovieDataFromJson(String jsonResponse) throws Exception {
            List<Movie> movies = new ArrayList<>();
            final String RESULTS_KEY = "results";
            final String POSTER_PATH = "poster_path";
            final String MOVIE_ID = "id";
            final String MOVIE_TITLE = "original_title";
            final String MOVIE_OVERVIEW = "overview";
            final String MOVIE_RATING = "vote_average";
            final String MOVIE_RELEASE_DATE = "release_date";


            try {
                JSONObject movieJSON = new JSONObject(jsonResponse);
                if (movieJSON.has(RESULTS_KEY)) {
                    JSONArray movieList = movieJSON.getJSONArray(RESULTS_KEY);
                    if (movieList.length() > 0) {
                        for (int i = 0; i < movieList.length(); i++) {
                            JSONObject movieDetailJSON = (JSONObject) movieList.get(i);
                            if (movieDetailJSON.has(POSTER_PATH) && movieDetailJSON.has(MOVIE_ID)) {
                                String posterPath = movieDetailJSON.getString(POSTER_PATH);
                                String movieID = movieDetailJSON.getString(MOVIE_ID);
                                Movie movie = new Movie(movieID, posterPath);
                                if (movieDetailJSON.has(MOVIE_TITLE)) {
                                    movie.setOriginalTitle(movieDetailJSON.getString(MOVIE_TITLE));
                                }
                                if (movieDetailJSON.has(MOVIE_OVERVIEW)) {
                                    movie.setOverview(movieDetailJSON.getString(MOVIE_OVERVIEW));
                                }
                                if (movieDetailJSON.has(MOVIE_RATING)) {
                                    movie.setVoteAverage(movieDetailJSON.getString(MOVIE_RATING));
                                }
                                if (movieDetailJSON.has(MOVIE_RELEASE_DATE)) {
                                    movie.setReleaseDate(movieDetailJSON.getString(MOVIE_RELEASE_DATE));
                                }
                                movies.add(movie);
                            }

                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), getString(R.string.movie_err_msg), Toast.LENGTH_SHORT).show();
            }

            return movies;
        }
    }
}
