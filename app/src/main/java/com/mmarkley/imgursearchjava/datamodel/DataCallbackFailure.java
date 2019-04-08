package com.mmarkley.imgursearchjava.datamodel;

public class DataCallbackFailure {
    private DataCallbackErrors status;
    private String errorText;

    public DataCallbackFailure(DataCallbackErrors status, String errorText) {
        this.status = status;
        this.errorText = errorText;
    }

    public DataCallbackErrors getStatus() {
        return status;
    }

    public void setStatus(DataCallbackErrors status) {
        this.status = status;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }



}
