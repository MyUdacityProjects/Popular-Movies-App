package com.example.android.popular_movies_app.models;

import java.util.List;

/**
 * @author harshita.k
 */
public class TrailersList {
    private String id;
    private String quicktime;
    private List<Trailer> youtube;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuicktime() {
        return quicktime;
    }

    public void setQuicktime(String quicktime) {
        this.quicktime = quicktime;
    }

    public List<Trailer> getYoutube() {
        return youtube;
    }

    public void setYoutube(List<Trailer> youtube) {
        this.youtube = youtube;
    }
}
