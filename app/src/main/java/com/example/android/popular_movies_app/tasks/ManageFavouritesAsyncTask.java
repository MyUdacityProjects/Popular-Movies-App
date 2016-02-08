package com.example.android.popular_movies_app.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

import com.example.android.popular_movies_app.R;
import com.example.android.popular_movies_app.db.DbUtils;
import com.example.android.popular_movies_app.models.Movie;

/**
 * @author harshita.k
 */
public class ManageFavouritesAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    private Movie movie;
    private Boolean performAction;
    private Button favButton;

    public ManageFavouritesAsyncTask(Context mContext, Movie movie, Boolean performAction, Button favButton) {
        this.mContext = mContext;
        this.movie = movie;
        this.performAction = performAction;
        this.favButton = favButton;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return DbUtils.isFavourite(mContext, movie.getId());
    }


    @Override
    protected void onPostExecute(Boolean isFavorited) {
        if (performAction) {
            new UpdateFavouritesAsyncTask(mContext, movie, isFavorited, favButton).execute();
        } else {
            if (isFavorited) {
                favButton.setText(mContext.getString(R.string.mark_unfavorite));
            } else {
                favButton.setText(mContext.getString(R.string.mark_favorite));
            }
        }
    }
}
