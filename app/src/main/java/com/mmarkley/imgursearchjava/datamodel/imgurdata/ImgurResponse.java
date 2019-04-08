package com.mmarkley.imgursearchjava.datamodel.imgurdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImgurResponse {
    @SerializedName("data")
    @Expose
    private List<ImgurDataObject> data;

    public List<ImgurDataObject> getData() {
        return data;
    }

    public void setData(List<ImgurDataObject> data) {
        this.data = data;
    }
}
