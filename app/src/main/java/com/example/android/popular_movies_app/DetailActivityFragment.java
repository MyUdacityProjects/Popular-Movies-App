package com.example.android.popular_movies_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

        movieTitleTextView = (TextView)rootView.findViewById(R.id.movie_title);
        movieOverviewTextView = (TextView) rootView.findViewById(R.id.movie_desc);
        movieRatingTextView = (TextView) rootView.findViewById(R.id.movie_rating);
        movieReleaseDateTextView = (TextView) rootView.findViewById(R.id.movie_release_date);
        moviePosterImageView = (ImageView) rootView.findViewById(R.id.movie_poster);

        movieDetail = getActivity().getIntent().getExtras().getParcelable("MOVIE");
        if(movieDetail != null){
            movieTitleTextView.setText(movieDetail.getOriginalTitle());
            movieOverviewTextView.setText(movieDetail.getOverview());
            movieRatingTextView.setText(movieDetail.getRating());
            movieReleaseDateTextView.setText(movieDetail.getMovieReleaseDate());
            Picasso.with(getContext()).load(movieDetail.getImageFullURL()).placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder).into(moviePosterImageView);
        }
        return rootView;
    }
}
