package com.example.a.webview.RESTService;

/**
 * Created by 34011-82-08 on 19/12/2016.
 */
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;

public interface RetrofitInterface {

    @GET("/")
    @Streaming
    Call<ResponseBody> downloadFile();
}
