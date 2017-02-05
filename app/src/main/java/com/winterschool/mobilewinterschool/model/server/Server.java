package com.winterschool.mobilewinterschool.model.server;

import android.os.Handler;
import android.os.Message;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by alek on 03.02.17.
 */

public class Server {

    public static final int ACK_LOGIN = 0;
    public static final int ERR_LOGIN = 1;

    public static final int ERR_CONNECTION = 2;

    public static final int ACK_PHOTO = 0;
    public static final int ERR_PHOTO = 1;

    public static final int ACK_PULSE = 0;
    public static final int ERR_PULSE = 1;

    private static final int ACK_STOP = 0;
    private static final int ERR_INVALID_SESSION = 1;
    private static final int ERR_END_SESSION = 3;

    private static Server mServer;
    private Retrofit retrofit;

    private Server() {
        retrofit = new Retrofit.Builder().baseUrl("http://svet.akadempark.com:12202").addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static Server getInstance(){
        if (mServer == null) {
            mServer = new Server();
        }
        return mServer;
    }

    public interface ServerAPI{
        @Headers("Content-Type: application/x-www-form-urlencoded")
        @FormUrlEncoded
        @POST("connect/token")
        Call<TokenResponse> postLoginPassword(@Field("grant_type") String grantType, @Field("username") String userName, @Field("password") String password);

        @Headers("Content-Type: application/json")
        @POST("photo")
        Call<Integer> postPhoto(@Header("Authorization") String token, @Body PhotoRequest photoRequest);

        @Headers("Content-Type: application/json")
        @POST("pulse")
        Call<Integer> postPulse(@Header("Authorization") String token, @Body PulseRequest pulseRequest);

        @Headers("Content-Type: application/json")
        @POST("finished")
        Call<Integer> postStopSignal(@Header("Authorization") String token, @Body StopSignalRequest stopRequest);
    }

    public void authRequest(String userName, String password, Handler handler){
        Message message = new Message();
        try {
             Response response = retrofit.create(ServerAPI.class).postLoginPassword("password", userName, password).execute();
             if (response.code() == 409)
                 message.arg1 = ERR_LOGIN;
             else {
                 message.arg1 = ACK_LOGIN;
                 TokenResponse tokenResponse = (TokenResponse) response.body();
                 message.obj = tokenResponse.getAccessToken();
             }
        } catch (IOException e) {message.arg1 = ERR_CONNECTION;}
        handler.handleMessage(message);
    }

    public void photoRequest(String imagePath, String timestamp, String token, Handler handler) {
       PhotoRequest photoRequest = new PhotoRequest(toBase64String(imagePath), timestamp);
        Message message = new Message();
        try {
            Response response = retrofit.create(ServerAPI.class).postPhoto("Bearer " + token, photoRequest).execute();
            if (response.code() != 200)
                message.arg1 = ERR_PHOTO;
            else {
                message.arg1 = ACK_PHOTO;
                message.obj = (Integer) response.body();
            }
        } catch (IOException e) {message.arg1 = ERR_CONNECTION;}
        handler.handleMessage(message);
    }

    public void pulseRequest(int pulse, int sessionId, String timestamp, String token, Handler handler) {
        PulseRequest pulseRequest = new PulseRequest(pulse, sessionId, timestamp);
        Message message = new Message();
        try {
            Response response = retrofit.create(ServerAPI.class).postPulse("Bearer " + token, pulseRequest).execute();
            if (response.code() != 200)
                message.arg1 = ERR_PULSE;
            else {
                message.arg1 = ACK_PULSE;
                message.obj = (Integer) response.body();
            }
        } catch (IOException e) {message.arg1 = ERR_CONNECTION;}
        handler.handleMessage(message);
    }

    public void stopSignalRequest(int sessionId, String token, Handler handler){
        StopSignalRequest stopSignalRequest = new StopSignalRequest(sessionId);
        Message message = new Message();
        try {
            Response response = retrofit.create(ServerAPI.class).postStopSignal("Bearer " + token, stopSignalRequest).execute();
            if (response.code() == 400)
                message.arg1 = ERR_INVALID_SESSION;
            else
                if (response.code() == 409)
                    message.arg1 = ERR_END_SESSION;
                else {
                    message.arg1 = ACK_STOP;
                    message.obj = (Integer) response.body();
                }
        } catch (IOException e) {message.arg1 = ERR_CONNECTION;}
        handler.handleMessage(message);
    }

    public String toBase64String(String imagePath){
        File file = new File(imagePath);
        FileInputStream fileInputStream = null;
        byte[] bytes = new byte[(int) file.length()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            fileInputStream.close();
        }
        catch(IOException e) {}
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}