package com.example.android.popular_movies_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popular_movies_app.adapters.ReviewAdapter;
import com.example.android.popular_movies_app.adapters.TrailerAdapter;
import com.example.android.popular_movies_app.models.ListResponse;
import com.example.android.popular_movies_app.models.Movie;
import com.example.android.popular_movies_app.models.Review;
import com.example.android.popular_movies_app.models.Trailer;
import com.example.android.popular_movies_app.models.TrailersList;
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

    public TrailerAdapter trailerAdapter;

    public MovieService movieService;

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
        final List<Review> reviews = new ArrayList<>();
        final List<Trailer> trailers = new ArrayList<>();

        reviewAdapter = new ReviewAdapter(getActivity(), reviews);
        trailerAdapter = new TrailerAdapter(getActivity(), trailers);

        ListView reviewList = (ListView) rootView.findViewById(R.id.reviewlist);
        reviewList.setAdapter(reviewAdapter);

        ListView trailerList = (ListView) rootView.findViewById(R.id.trailerlist);
        trailerList.setAdapter(trailerAdapter);

        trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String youtubeVideoId = trailers.get(position).getKey();
                String videoURI = "vnd.youtube:" + youtubeVideoId;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(videoURI));
                startActivity(i);
            }
        });

        movieService = MovieClient.createService(MovieService.class);

        fetchReviews();
        fetchTrailers();

        return rootView;
    }

    private void fetchReviews() {
        Call<ListResponse<Review>> reviewCall = movieService.getMovieReviews(movieDetail.getId());
        reviewCall.enqueue(new Callback<ListResponse<Review>>() {
            @Override
            public void onResponse(Response<ListResponse<Review>> response) {
                List<Review> reviews = response.body().getResults();
                reviewAdapter.clear();
                for (Review review : reviews) {
                    reviewAdapter.add(review);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), "Throw up", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchTrailers() {
        Call<TrailersList> trailersListCall = movieService.getMovieTrailers(movieDetail.getId());
        trailersListCall.enqueue(new Callback<TrailersList>() {
            @Override
            public void onResponse(Response<TrailersList> response) {
                List<Trailer> trailers = response.body().getResults();
                trailerAdapter.clear();
                for (Trailer trailer : trailers) {
                    trailerAdapter.add(trailer);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), "Throw up", Toast.LENGTH_LONG).show();
            }
        });
    }
}
