package com.example.ssalameh.volleyhttpproject;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import java.io.BufferedReader;
import java.io.InputStreamReader;
public class HttpRequest extends AsyncTask<Integer,Void,String>{
    String category,city,FinalResult;
    public HttpRequest(String NewCity,String NewCategory){
        category=NewCategory;
        city=NewCity;
        FinalResult="";
    }
    @Override
    protected String doInBackground(Integer...limit){
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://127.0.0.1:8080/locations?city="+city+"&category="+category+"&limit="+limit[0]);
            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                FinalResult+=line;
            }
        }catch(Exception e){
            Log.d("Exception",e.getMessage()+" ");
        }
        return FinalResult;
    }
}
