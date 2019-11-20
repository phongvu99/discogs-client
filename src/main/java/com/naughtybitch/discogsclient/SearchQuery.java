package com.naughtybitch.discogsclient;



public class SearchQuery {

    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    protected SearchQuery(String query) {
        this.query = query;
    }
}

