package com.winterschool.mobilewinterschool.controller.server;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alek on 03.02.17.
 */
public class PhotoRequest {
    @SerializedName("image")
    private String image;

    @SerializedName("timestamp")
    private String timestamp;

    public PhotoRequest(String image, String timestamp) {
        this.image = image;
        this.timestamp = timestamp;
    }
}
