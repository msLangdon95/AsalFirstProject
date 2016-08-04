package com.example.ssalameh.volleyhttpproject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

class Bucket implements Serializable {
    ArrayList<String> Names;
    ArrayList<String> PicURL;
    boolean EmptyFlag;
    public Bucket() {
        Names = new ArrayList<String>();
        PicURL = new ArrayList<String>();
        EmptyFlag=false;
    }
}

public class Parser extends AsyncTask<String, Void, Bucket> {
    int limit;
    Bucket obj;
    String error = "[]";

    public Parser() {
        obj = new Bucket();
    }

    @Override
    protected Bucket doInBackground(String... x){
        if(x[0].equals("wrong")){
            NotingToDo();
            Log.d("STATE","WRONG");
        }
        else {
            try {
                JSONObject jsonObj = new JSONObject(x[0]);
                JSONArray jsonArr = jsonObj.getJSONObject("response").getJSONArray("groups").getJSONObject(0).getJSONArray("items");
                limit = jsonArr.length();
                if (limit == 0) {
                    obj.Names.add("Sorry, No Results");
                    obj.PicURL.add("http://coyotepromotions.com.ismmedia.com/ISM3/thumbcache/5d29d8ff0f16e0e1028034d3c001ac33.300.jpg");
                    obj.EmptyFlag = true;
                } else {

                    int i;
                    for (i = 0; i < limit; i++) {
                        obj.Names.add(jsonArr.getJSONObject(i).getJSONObject("venue").getString("name"));
                        if (!error.equals(String.valueOf(jsonArr.getJSONObject(i).getJSONObject("venue").getJSONObject("photos").getJSONArray("groups")))) {
                            obj.PicURL.add((jsonArr.getJSONObject(i).getJSONObject("venue").getJSONObject("photos").getJSONArray("groups").getJSONObject(0).getJSONArray("items").getJSONObject(0).getString("prefix")) + "250x250" + (jsonArr.getJSONObject(i).getJSONObject("venue").getJSONObject("photos").getJSONArray("groups").getJSONObject(0).getJSONArray("items").getJSONObject(0).getString("suffix")));
                        } else {
                            obj.PicURL.add("http://coyotepromotions.com.ismmedia.com/ISM3/thumbcache/5d29d8ff0f16e0e1028034d3c001ac33.300.jpg");
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("STATE", "Exception in ParserDoInBackground");
            }
        }
        return obj;
    }
    void NotingToDo(){
        obj.Names.add("Sorry, No Results");
        obj.PicURL.add("http://coyotepromotions.com.ismmedia.com/ISM3/thumbcache/5d29d8ff0f16e0e1028034d3c001ac33.300.jpg");
        obj.EmptyFlag=true;
    }
}
