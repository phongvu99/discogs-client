package com.naughtybitch.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MasterResponse {

    @SerializedName("genres")
    @Expose
    private List<String> genres = null;
    @SerializedName("num_for_sale")
    @Expose
    private Integer numForSale;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("most_recent_release")
    @Expose
    private Integer mostRecentRelease;
    @SerializedName("main_release")
    @Expose
    private Integer mainRelease;
    @SerializedName("main_release_url")
    @Expose
    private String mainReleaseUrl;
    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("versions_url")
    @Expose
    private String versionsUrl;
    @SerializedName("tracklist")
    @Expose
    private List<Tracklist> tracklist = null;
    @SerializedName("most_recent_release_url")
    @Expose
    private String mostRecentReleaseUrl;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("resource_url")
    @Expose
    private String resourceUrl;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("data_quality")
    @Expose
    private String dataQuality;

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public Integer getNumForSale() {
        return numForSale;
    }

    public void setNumForSale(Integer numForSale) {
        this.numForSale = numForSale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMostRecentRelease() {
        return mostRecentRelease;
    }

    public void setMostRecentRelease(Integer mostRecentRelease) {
        this.mostRecentRelease = mostRecentRelease;
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getVersionsUrl() {
        return versionsUrl;
    }

    public void setVersionsUrl(String versionsUrl) {
        this.versionsUrl = versionsUrl;
    }

    public List<Tracklist> getTracklist() {
        return tracklist;
    }

    public void setTracklist(List<Tracklist> tracklist) {
        this.tracklist = tracklist;
    }

    public String getMostRecentReleaseUrl() {
        return mostRecentReleaseUrl;
    }

    public void setMostRecentReleaseUrl(String mostRecentReleaseUrl) {
        this.mostRecentReleaseUrl = mostRecentReleaseUrl;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDataQuality() {
        return dataQuality;
    }

    public void setDataQuality(String dataQuality) {
        this.dataQuality = dataQuality;
    }

}
