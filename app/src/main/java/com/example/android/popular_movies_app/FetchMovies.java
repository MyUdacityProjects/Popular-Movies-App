package com.example.android.popular_movies_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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
 * Created by harshita.k on 22/01/16.
 */
public class FetchMovies extends AsyncTask<String, Void, List<Movie>> {

    private Context context;
    private MovieAdapter mMovieAdapter;
    private ProgressDialog dialog;

    public FetchMovies(Context context, MovieAdapter mMovieAdapter, ProgressDialog dialog) {
        this.context = context;
        this.mMovieAdapter = mMovieAdapter;
        this.dialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        if (dialog == null) {
            dialog = new ProgressDialog(context);
            dialog.setMessage(context.getString(R.string.loading_msg));
            dialog.show();
        }
        super.onPreExecute();
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        String responseJSONStr = null;
        List<Movie> moviesList = new ArrayList<>();
        String sort_order = params[0];

        try {

            Uri uri = Uri.parse(APIConstants.OPEN_DB_BASE_URL).buildUpon().
                    appendQueryParameter(APIConstants.SORT_QUERY_PARAM, sort_order).
                    appendQueryParameter(APIConstants.APPKEY_QUERY_PARAM, BuildConfig.THE_MOVIEDB_API_KEY).build();

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
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        mMovieAdapter.clear();
        for (Movie movie : movies) {
            mMovieAdapter.add(movie);
        }
    }

    private List<Movie> getMovieDataFromJson(String jsonResponse) throws Exception {
        List<Movie> movies = new ArrayList<>();


        try {
            JSONObject movieJSON = new JSONObject(jsonResponse);
            if (movieJSON.has(APIConstants.RESULTS_KEY)) {
                JSONArray movieList = movieJSON.getJSONArray(APIConstants.RESULTS_KEY);
                if (movieList.length() > 0) {
                    for (int i = 0; i < movieList.length(); i++) {
                        JSONObject movieDetailJSON = (JSONObject) movieList.get(i);
                        if (movieDetailJSON.has(APIConstants.POSTER_PATH) && movieDetailJSON.has(APIConstants.MOVIE_ID)) {
                            String posterPath = movieDetailJSON.getString(APIConstants.POSTER_PATH);
                            String movieID = movieDetailJSON.getString(APIConstants.MOVIE_ID);
                            Movie movie = new Movie(movieID, posterPath);
                            if (movieDetailJSON.has(APIConstants.MOVIE_TITLE)) {
                                movie.setOriginalTitle(movieDetailJSON.getString(APIConstants.MOVIE_TITLE));
                            }
                            if (movieDetailJSON.has(APIConstants.MOVIE_OVERVIEW)) {
                                movie.setOverview(movieDetailJSON.getString(APIConstants.MOVIE_OVERVIEW));
                            }
                            if (movieDetailJSON.has(APIConstants.MOVIE_RATING)) {
                                movie.setVoteAverage(movieDetailJSON.getString(APIConstants.MOVIE_RATING));
                            }
                            if (movieDetailJSON.has(APIConstants.MOVIE_RELEASE_DATE)) {
                                movie.setReleaseDate(movieDetailJSON.getString(APIConstants.MOVIE_RELEASE_DATE));
                            }
                            movies.add(movie);
                        }

                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.movie_err_msg), Toast.LENGTH_SHORT).show();
        }

        return movies;
    }
}
