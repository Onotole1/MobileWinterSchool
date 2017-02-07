package com.winterschool.mobilewinterschool.controller.server;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alek on 03.02.17.
 */
public class StopSignalRequest {
    @SerializedName("sessionId")
    private int sessionId;

    public StopSignalRequest(int sessionId){
        this.sessionId = sessionId;
    }
}
