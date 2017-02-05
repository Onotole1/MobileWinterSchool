package com.winterschool.mobilewinterschool.model.server;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alek on 04.02.17.
 */
public class AuthRequest {
    @SerializedName("grant_type")
    private String grantType;

    @SerializedName("username")
    private String userName;

    @SerializedName("password")
    private String password;

    public AuthRequest(String userName, String password) {
        this.grantType = "password";
        this.userName = userName;
        this.password = password;
    }
}
