package com.example.android.popular_movies_app.models;

import java.util.List;

/**
 * @author harshita.k
 */
public class TrailersList {
    private String id;

    private List<Trailer> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }


}
