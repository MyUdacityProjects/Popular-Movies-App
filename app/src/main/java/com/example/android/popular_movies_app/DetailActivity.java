package com.example.android.popular_movies_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.popular_movies_app.utils.Constants;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(Constants.MOVIE_TAG,
                    getIntent().getParcelableExtra(Constants.MOVIE_TAG));

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }

}
