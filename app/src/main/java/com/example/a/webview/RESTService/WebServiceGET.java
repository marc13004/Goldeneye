package com.example.a.webview.RESTService;

/**
 * Created by 34011-82-08 on 28/11/2016.
 */


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;


public class WebServiceGET extends AsyncTask<String,String,String>{

    public static int httpStatus;
    protected static HttpURLConnection urlConnection = null;
    public static JSONObject jo = null;

    @Override
    protected String doInBackground(String... params)
    {

        InputStream stream = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(params[0]);

            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("172.19.254.1", 8080));

            // Ouverture de la connexion
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connexion à l'URL
            urlConnection.connect();
            Log.i("getResponseCode()",urlConnection.getResponseCode()+"");
            // Si le serveur nous répond avec un code OK
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = urlConnection.getInputStream();
                httpStatus = urlConnection.getResponseCode();
                Log.i("WebService ok", httpStatus+"");

            }else{
                httpStatus = urlConnection.getResponseCode();
                Log.i("WebService pas ok", httpStatus+"");
            }
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED) {

            }
            if (stream != null) {
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                try {
                    jo = new JSONObject(buffer.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return buffer.toString();
            }
            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }finally
        {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return null;
    }
    public static void disconnect(){
        urlConnection.disconnect();
    }

}
