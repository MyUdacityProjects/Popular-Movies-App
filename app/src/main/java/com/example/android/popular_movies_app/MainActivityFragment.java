package com.example.android.popular_movies_app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        List<Movie> movies = new ArrayList<>();

        mMovieAdapter = new MovieAdapter(getActivity(),movies);

        GridView movieGrid = (GridView)rootView.findViewById(R.id.movie_grid);
        movieGrid.setAdapter(mMovieAdapter);
        FetchMovies fetchMovies = new FetchMovies();
        fetchMovies.execute();
        return rootView;
    }

    public class FetchMovies extends AsyncTask<Void, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(Void... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            String responseJSONStr = null;
            List<Movie> moviesList = null;

            try {

                final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_QUERY_PARAM = "sort_by";
                final String APPKEY_QUERY_PARAM = "api_key";

                Uri uri = Uri.parse(BASE_URL).buildUpon().
                        appendQueryParameter(SORT_QUERY_PARAM, "popularity.desc").
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
                    return null;
                }
                responseJSONStr = buffer.toString();


            } catch (Exception e) {
                Log.e("Fetch Error", "Error ", e);
                return moviesList;

            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
                        Log.e("Fetch Error", "Error closing stream", e);
                        return moviesList;
                    }
                }
                try {
                    moviesList = getMovieDataFromJson(responseJSONStr);
                } catch (Exception e) {
                    Log.e("FORECAST FRAGMENT", "Error parsing JSON", e);
                    return moviesList;
                }

            }

            return moviesList;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            mMovieAdapter.clear();
            for (Movie movie : movies){
                mMovieAdapter.add(movie);
            }
        }

        private List<Movie> getMovieDataFromJson(String jsonResponse){
            List<Movie> movies = new ArrayList<>();
            movies.add(new Movie("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"));
            movies.add(new Movie("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"));
            movies.add(new Movie("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"));
            movies.add(new Movie("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"));
            movies.add(new Movie("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"));
            movies.add(new Movie("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"));
            return  movies;
        }
    }
}
