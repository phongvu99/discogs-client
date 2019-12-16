package com.naughtybitch.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("catno")
    @Expose
    private String catno;
    @SerializedName("formats")
    @Expose
    private List<Format> formats = null;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("style")
    @Expose
    private List<String> style = null;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("label")
    @Expose
    private List<String> label = null;
    @SerializedName("master_id")
    @Expose
    private Integer masterId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("format")
    @Expose
    private List<String> format = null;
    @SerializedName("barcode")
    @Expose
    private List<String> barcode = null;
    @SerializedName("master_url")
    @Expose
    private String masterUrl;
    @SerializedName("genre")
    @Expose
    private List<String> genre = null;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("cover_image")
    @Expose
    private String coverImage;
    @SerializedName("resource_url")
    @Expose
    private String resourceUrl;
    @SerializedName("format_quantity")
    @Expose
    private Integer formatQuantity;

    public Result(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getCatno() {
        return catno;
    }

    public void setCatno(String catno) {
        this.catno = catno;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getStyle() {
        return style;
    }

    public void setStyle(List<String> style) {
        this.style = style;
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

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
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

    public List<String> getBarcode() {
        return barcode;
    }

    public void setBarcode(List<String> barcode) {
        this.barcode = barcode;
    }

    public String getMasterUrl() {
        return masterUrl;
    }

    public void setMasterUrl(String masterUrl) {
        this.masterUrl = masterUrl;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public Integer getFormatQuantity() {
        return formatQuantity;
    }

    public void setFormatQuantity(Integer formatQuantity) {
        this.formatQuantity = formatQuantity;
    }

    public List<Format> getFormats() {
        return formats;
    }

    public void setFormats(List<Format> formats) {
        this.formats = formats;
    }

}