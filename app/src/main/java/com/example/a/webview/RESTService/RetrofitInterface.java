package com.example.a.webview.RESTService;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;


public interface RetrofitInterface {

    @GET("/")
    @Streaming
    Call<ResponseBody> downloadFile();
}
