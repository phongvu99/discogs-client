package com.naughtybitch.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MasterReleasesResponse {

    @SerializedName("styles")
    @Expose
    private List<String> styles = null;
    @SerializedName("genres")
    @Expose
    private List<String> genres = null;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("main_release")
    @Expose
    private Integer mainRelease;
    @SerializedName("main_release_url")
    @Expose
    private String mainReleaseUrl;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("artists")
    @Expose
    private List<Artist> artists = null;
    @SerializedName("versions_url")
    @Expose
    private String versionsUrl;
    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("resource_url")
    @Expose
    private String resourceUrl;
    @SerializedName("tracklist")
    @Expose
    private List<Tracklist> tracklist = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("num_for_sale")
    @Expose
    private Integer numForSale;
    @SerializedName("data_quality")
    @Expose
    private String dataQuality;

    public List<String> getStyles() {
        return styles;
    }

    public void setStyles(List<String> styles) {
        this.styles = styles;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMainRelease() {
        return mainRelease;
    }

    public void setMainRelease(Integer mainRelease) {
        this.mainRelease = mainRelease;
    }

    public String getMainReleaseUrl() {
        return mainReleaseUrl;
    }

    public void setMainReleaseUrl(String mainReleaseUrl) {
        this.mainReleaseUrl = mainReleaseUrl;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getVersionsUrl() {
        return versionsUrl;
    }

    public void setVersionsUrl(String versionsUrl) {
        this.versionsUrl = versionsUrl;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public List<Tracklist> getTracklist() {
        return tracklist;
    }

    public void setTracklist(List<Tracklist> tracklist) {
        this.tracklist = tracklist;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumForSale() {
        return numForSale;
    }

    public void setNumForSale(Integer numForSale) {
        this.numForSale = numForSale;
    }

    public String getDataQuality() {
        return dataQuality;
    }

    public void setDataQuality(String dataQuality) {
        this.dataQuality = dataQuality;
    }

}