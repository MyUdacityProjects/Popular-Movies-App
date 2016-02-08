package com.example.android.popular_movies_app.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.android.popular_movies_app.R;
import com.example.android.popular_movies_app.db.MovieContracts;
import com.example.android.popular_movies_app.utils.Constants;
import com.squareup.picasso.Picasso;

public class MovieCursorAdapter extends CursorAdapter {
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.movie_grid_item, parent, false);
    }

    private String getMoviePosterFromCursor(Cursor cursor){
        int poster_idx = cursor.getColumnIndex(MovieContracts.MOVIES_TABLE.COLUMN_POSTER_IMAGE);
        return cursor.getString(poster_idx);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView moviePoster = (ImageView) view.findViewById(R.id.movie_poster);
        String posterURL = Constants.APIConstants.IMAGE_BASE_URL + Constants.APIConstants.IMAGE_SMALL_SIZE + getMoviePosterFromCursor(cursor);
        Picasso.with(context).load(posterURL).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(moviePoster);
        return;
    }

    public MovieCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }
}
