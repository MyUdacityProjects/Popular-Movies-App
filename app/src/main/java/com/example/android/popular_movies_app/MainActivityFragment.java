package com.example.android.popular_movies_app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
        switch (item.getItemId()){
            case R.id.action_sort_popular:
                sort_order = "popularity.desc";
                new FetchMovies().execute(sort_order);
                return  true;
            case R.id.action_sort_rating:
                sort_order = "vote_average.desc";
                new FetchMovies().execute(sort_order);
                return  true;
            default:
                return true;
        }
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
        fetchMovies.execute("popularity.desc");
        return rootView;
    }

    public class FetchMovies extends AsyncTask<String, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            String responseJSONStr = null;
            List<Movie> moviesList = null;
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

        private List<Movie> getMovieDataFromJson(String jsonResponse) throws Exception{
            List<Movie> movies = new ArrayList<>();
            final String RESULTS_KEY = "results";
            final String POSTER_PATH = "poster_path";
            try{
                JSONObject movieJSON = new JSONObject(jsonResponse);
                if(movieJSON.has(RESULTS_KEY)){
                    JSONArray movieList = movieJSON.getJSONArray(RESULTS_KEY);
                    if(movieList.length() > 0){
                        for(int i = 0; i < movieList.length(); i++) {
                            JSONObject movie = (JSONObject)movieList.get(i);
                            if(movie.has(POSTER_PATH)){
                                String posterPath = movie.getString(POSTER_PATH);
                                movies.add(new Movie(posterPath));
                            }

                        }
                    }
                }
            }catch (Exception e){
                Toast.makeText(getActivity(),"Error in JSON",Toast.LENGTH_SHORT).show();
            }

            return  movies;
        }
    }
}