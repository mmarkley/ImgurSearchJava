package com.mmarkley.imgursearchjava.datamodel.imgurdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImgurProcessing {
    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
