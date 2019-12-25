package com.naughtybitch.POJOlastfm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopTracksResponse {

    @SerializedName("tracks")
    @Expose
    private Tracks tracks;

    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }

}