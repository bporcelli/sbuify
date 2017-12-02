package com.cse308.sbuify.image;

public class Base64Image implements ImageI {

    /** Data URL containing base64-encoded image data. */
    private String dataURL;

    public String getDataURL() {
        return dataURL;
    }

    public void setDataURL(String dataURL) {
        this.dataURL = dataURL;
    }
}
