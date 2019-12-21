package com.naughtybitch.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WantlistResponse {

    @SerializedName("wants")
    @Expose
    private List<Want> wants = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    public List<Want> getWants() {
        return wants;
    }

    public void setWants(List<Want> wants) {
        this.wants = wants;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

}
