package com.example.ssalameh.volleyhttpproject;
import android.os.AsyncTask;
import android.util.Log;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
public class LanguageTranslation extends AsyncTask<String, Void, String> {
    String City;
    public LanguageTranslation(){
        City="";
    }
    protected void onPreExecute(){
        Translate.setClientId("myApplicationIDSameeraSalameh");
        Translate.setClientSecret("VL3TzT/+QvEjJeLNIsQHS2mUPE2owzn+an2EHtOwJJc=");
    }
    protected String doInBackground(String...x){
        try {
            City = Translate.execute(x[0], Language.AUTO_DETECT, Language.ENGLISH);
            Log.d("translatedText",City+" ");
        }catch(Exception e){
            Log.d("state",e.getMessage()+" ?ERROR while translating");
        }
        return City;
    }
}
