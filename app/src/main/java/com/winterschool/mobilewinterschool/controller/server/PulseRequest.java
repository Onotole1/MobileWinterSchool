package com.winterschool.mobilewinterschool.controller.server;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alek on 03.02.17.
 */
public class PulseRequest {
    @SerializedName("pulse")
    private int pulse;

    @SerializedName("sessionId")
    private int sessionId;

    @SerializedName("timestamp")
    private String timestamp;

    public PulseRequest(int pulse, int sessionId, String timestamp) {
        this.pulse = pulse;
        this.sessionId = sessionId;
        this.timestamp = timestamp;
    }
}
