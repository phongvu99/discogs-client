package com.naughtybitch.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CollectionValueResponse {

    @SerializedName("minimum")
    @Expose
    private String minimum;
    @SerializedName("median")
    @Expose
    private String median;
    @SerializedName("maximum")
    @Expose
    private String maximum;

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getMedian() {
        return median;
    }

    public void setMedian(String median) {
        this.median = median;
    }

    public String getMaximum() {
        return maximum;
    }

    public void setMaximum(String maximum) {
        this.maximum = maximum;
    }

}
