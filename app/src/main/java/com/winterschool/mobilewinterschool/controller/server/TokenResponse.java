package com.winterschool.mobilewinterschool.controller.server;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alek on 03.02.17.
 */
public class TokenResponse {
    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("expires_in")
    private String expiresIn;

    public String getAccessToken(){
        return accessToken;
    }
}
