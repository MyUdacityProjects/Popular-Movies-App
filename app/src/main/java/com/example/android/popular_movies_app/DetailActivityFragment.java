package com.example.android.popular_movies_app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public Movie movieDetail;
    public TextView movieTitleTextView,movieOverviewTextView,movieRatingTextView,movieReleaseDateTextView;
    public ImageView moviePosterImageView;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        String movieID = getActivity().getIntent().getStringExtra("MOVIE_ID");
        movieTitleTextView = (TextView)rootView.findViewById(R.id.movie_title);
        movieOverviewTextView = (TextView) rootView.findViewById(R.id.movie_desc);
        movieRatingTextView = (TextView) rootView.findViewById(R.id.movie_rating);
        movieReleaseDateTextView = (TextView) rootView.findViewById(R.id.movie_release_date);
        moviePosterImageView = (ImageView) rootView.findViewById(R.id.movie_poster);
        new FetchMovieDetail().execute(movieID);
        return rootView;
    }

    public class FetchMovieDetail extends AsyncTask<String, Void, Movie> {
        @Override
        protected Movie doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            String responseJSONStr = null;
            Movie movieDetail = null;
            String movieID = params[0];

            try {

                final String BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String EXTERNAL_SOURCE_PARAM = "external_source";
                final String APPKEY_QUERY_PARAM = "api_key";
                final String EXTERNAL_SOURCE_VALUE = "imdb_id";

                Uri uri = Uri.parse(BASE_URL+movieID+"?").buildUpon().
                        appendQueryParameter(EXTERNAL_SOURCE_PARAM, EXTERNAL_SOURCE_VALUE).
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
                return movieDetail;

            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
                        Log.e("Fetch Error", "Error closing stream", e);
                        return movieDetail;
                    }
                }
                try {
                    movieDetail = getMovieDataFromJson(responseJSONStr);
                } catch (Exception e) {
                    Log.e("FORECAST FRAGMENT", "Error parsing JSON", e);
                    return movieDetail;
                }

            }

            return movieDetail;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);
            movieDetail = movie;
            if(movieDetail != null){
                movieTitleTextView.setText(movieDetail.getOriginalTitle());
                movieOverviewTextView.setText(movieDetail.getOverview());
                movieRatingTextView.setText(movieDetail.getVoteAverage());
                movieReleaseDateTextView.setText(movieDetail.getReleaseDate());
                Picasso.with(getContext()).load(movieDetail.getImageFullURL()).placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder).into(moviePosterImageView);
            }

        }

        private Movie getMovieDataFromJson(String jsonResponse) throws Exception{
            Movie movieDetail = null;
            final String POSTER_PATH = "poster_path";
            final String MOVIE_ID = "id";
            final String MOVIE_TITLE = "original_title";
            final String MOVIE_OVERVIEW = "overview";
            final String MOVIE_RATING = "vote_average";
            final String MOVIE_RELEASE_DATE = "release_date";
            try{
                JSONObject movieDetailJSON = new JSONObject(jsonResponse);
                if(movieDetailJSON.has(MOVIE_ID) && movieDetailJSON.has(POSTER_PATH)){
                    movieDetail = new Movie(movieDetailJSON.getString(MOVIE_ID),movieDetailJSON.getString(POSTER_PATH));
                    if(movieDetailJSON.has(MOVIE_TITLE)){
                        movieDetail.setOriginalTitle(movieDetailJSON.getString(MOVIE_TITLE));
                    }
                    if(movieDetailJSON.has(MOVIE_OVERVIEW)){
                        movieDetail.setOverview(movieDetailJSON.getString(MOVIE_OVERVIEW));
                    }
                    if(movieDetailJSON.has(MOVIE_RATING)){
                        movieDetail.setVoteAverage(movieDetailJSON.getString(MOVIE_RATING));
                    }
                    if(movieDetailJSON.has(MOVIE_RELEASE_DATE)){
                        movieDetail.setReleaseDate(movieDetailJSON.getString(MOVIE_RELEASE_DATE));
                    }
                }
            }catch (Exception e){
                Toast.makeText(getActivity(), "Error in JSON", Toast.LENGTH_SHORT).show();
            }

            return  movieDetail;
        }
    }
}
