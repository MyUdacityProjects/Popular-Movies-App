package com.example.android.popular_movies_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popular_movies_app.adapters.ReviewAdapter;
import com.example.android.popular_movies_app.models.ListResponse;
import com.example.android.popular_movies_app.models.Movie;
import com.example.android.popular_movies_app.models.Review;
import com.example.android.popular_movies_app.services.MovieClient;
import com.example.android.popular_movies_app.services.MovieService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    @Bind(R.id.movie_title)
    TextView movieTitleTextView;

    @Bind(R.id.movie_desc)
    TextView movieOverviewTextView;

    @Bind(R.id.movie_rating)
    TextView movieRatingTextView;

    @Bind(R.id.movie_release_date)
    TextView movieReleaseDateTextView;

    @Bind(R.id.movie_poster)
    ImageView moviePosterImageView;

    public Movie movieDetail;

    public ReviewAdapter reviewAdapter;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ButterKnife.bind(this, rootView);

        movieDetail = getActivity().getIntent().getExtras().getParcelable("MOVIE");
        if (movieDetail != null) {
            movieTitleTextView.setText(movieDetail.getOriginalTitle());
            movieOverviewTextView.setText(movieDetail.getOverview());
            movieRatingTextView.setText(movieDetail.getRating());
            movieReleaseDateTextView.setText(movieDetail.getMovieReleaseDate());
            Picasso.with(getContext()).load(movieDetail.getImageFullURL()).placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder).into(moviePosterImageView);
        }
        MovieService movieService = MovieClient.createService(MovieService.class);
        Call<ListResponse<Review>> reviewCall = movieService.getMovieReviews(movieDetail.getId());
        reviewCall.enqueue(new Callback<ListResponse<Review>>() {
            @Override
            public void onResponse(Response<ListResponse<Review>> response) {
                //Toast.makeText(getActivity(), "Yayee", Toast.LENGTH_LONG).show();
                List<Review> reviews = response.body().getResults();
                reviewAdapter.clear();
                for(Review review:reviews){
                    reviewAdapter.add(review);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), "Throw up", Toast.LENGTH_LONG).show();
            }
        });

        final List<Review> reviews = new ArrayList<>();

        reviewAdapter = new ReviewAdapter(getActivity(), reviews);

        ListView reviewList = (ListView) rootView.findViewById(R.id.reviewlist);
        reviewList.setAdapter(reviewAdapter);

        return rootView;
    }
}
