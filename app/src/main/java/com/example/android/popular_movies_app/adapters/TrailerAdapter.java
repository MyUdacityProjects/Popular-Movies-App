package com.example.android.popular_movies_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.popular_movies_app.R;
import com.example.android.popular_movies_app.models.Trailer;

import java.util.List;


public class TrailerAdapter extends ArrayAdapter<Trailer> {
    public TrailerAdapter(Context context, List<Trailer> trailers) {
        super(context, 0, trailers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_list_item, parent, false);
        }
        TextView trailerText = (TextView) convertView.findViewById(R.id.trailer_name);
        trailerText.setText(getContext().getString(R.string.trailer,(position + 1)));
        return convertView;
    }
}
