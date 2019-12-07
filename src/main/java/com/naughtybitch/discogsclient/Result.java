package com.naughtybitch.discogsclient;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("style")
    @Expose
    private List<String> style = null;
    @SerializedName("barcode")
    @Expose
    private List<String> barcode = null;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("format")
    @Expose
    private List<String> format = null;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("label")
    @Expose
    private List<String> label = null;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("cover_image")
    @Expose
    private String coverImage;
    @SerializedName("catno")
    @Expose
    private String catno;
    @SerializedName("master_url")
    @Expose
    private String masterUrl;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("genre")
    @Expose
    private List<String> genre = null;
    @SerializedName("resource_url")
    @Expose
    private String resourceUrl;
    @SerializedName("master_id")
    @Expose
    private Integer masterId;
    @SerializedName("format_quantity")
    @Expose
    private Integer formatQuantity;
    @SerializedName("id")
    @Expose
    private Integer id;

    public List<String> getStyle() {
        return style;
    }

    public void setStyle(List<String> style) {
        this.style = style;
    }

    public List<String> getBarcode() {
        return barcode;
    }

    public void setBarcode(List<String> barcode) {
        this.barcode = barcode;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getFormat() {
        return format;
    }

    public void setFormat(List<String> format) {
        this.format = format;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getCatno() {
        return catno;
    }

    public void setCatno(String catno) {
        this.catno = catno;
    }

    public String getMasterUrl() {
        return masterUrl;
    }

    public void setMasterUrl(String masterUrl) {
        this.masterUrl = masterUrl;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public Integer getFormatQuantity() {
        return formatQuantity;
    }

    public void setFormatQuantity(Integer formatQuantity) {
        this.formatQuantity = formatQuantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}