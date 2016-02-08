package com.example.android.popular_movies_app.models;

/**
 * @author harshita.k
 */
public class Review {
    private String id;
    private String author;
    private String url;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
        //int maxLength = (content.length() < Constants.APIConstants.MAX_CHAR_DISPLAY) ? content.length() : Constants.APIConstants.MAX_CHAR_DISPLAY;
        //return content.substring(0, maxLength).concat("...");
    }


    public void setContent(String content) {
        this.content = content;
    }
}
