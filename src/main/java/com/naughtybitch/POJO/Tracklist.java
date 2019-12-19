package com.naughtybitch.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tracklist {

    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("type_")
    @Expose
    private String type;
    @SerializedName("artists")
    @Expose
    private List<Artist> artists = null;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sub_tracks")
    @Expose
    private List<SubTrack> subTracks = null;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SubTrack> getSubTracks() {
        return subTracks;
    }

    public void setSubTracks(List<SubTrack> subTracks) {
        this.subTracks = subTracks;
    }

}

