package com.mmarkley.imgursearchjava.datamodel.imgurdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImgurAdConfig {

    @SerializedName("safeFlags")
    @Expose
    private List<String> safeFlags = null;
    @SerializedName("highRiskFlags")
    @Expose
    private List<Object> highRiskFlags = null;
    @SerializedName("unsafeFlags")
    @Expose
    private List<Object> unsafeFlags = null;
    @SerializedName("showsAds")
    @Expose
    private Boolean showsAds;

    public List<String> getSafeFlags() {
        return safeFlags;
    }

    public void setSafeFlags(List<String> safeFlags) {
        this.safeFlags = safeFlags;
    }

    public List<Object> getHighRiskFlags() {
        return highRiskFlags;
    }

    public void setHighRiskFlags(List<Object> highRiskFlags) {
        this.highRiskFlags = highRiskFlags;
    }

    public List<Object> getUnsafeFlags() {
        return unsafeFlags;
    }

    public void setUnsafeFlags(List<Object> unsafeFlags) {
        this.unsafeFlags = unsafeFlags;
    }

    public Boolean getShowsAds() {
        return showsAds;
    }

    public void setShowsAds(Boolean showsAds) {
        this.showsAds = showsAds;
    }
}
