package com.naughtybitch.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LabelResponse {

    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("releases_url")
    @Expose
    private String releasesUrl;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("parent_label")
    @Expose
    private ParentLabel parentLabel;
    @SerializedName("contact_info")
    @Expose
    private String contactInfo;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("sublabels")
    @Expose
    private List<Sublabel> sublabels = null;
    @SerializedName("urls")
    @Expose
    private List<String> urls = null;
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

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getReleasesUrl() {
        return releasesUrl;
    }

    public void setReleasesUrl(String releasesUrl) {
        this.releasesUrl = releasesUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<Sublabel> getSublabels() {
        return sublabels;
    }

    public void setSublabels(List<Sublabel> sublabels) {
        this.sublabels = sublabels;
    }

    public ParentLabel getParentLabel() {
        return parentLabel;
    }

    public void setParentLabel(ParentLabel parentLabel) {
        this.parentLabel = parentLabel;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
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
