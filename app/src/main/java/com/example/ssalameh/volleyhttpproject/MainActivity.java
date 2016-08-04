package com.example.ssalameh.volleyhttpproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;

class SendToSecondActivity implements Serializable {
    Bucket ParsedObject;
    String City;
    String Category;

    public SendToSecondActivity() {
        City = Category = "";
        ParsedObject = new Bucket();
    }
}

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    String Category, City, ParsedCity, BodyResult;
    SendToSecondActivity ToSend;
    Context context;
    int limit, PicCounter = 0;
    Button searchButton;
    String[] items = new String[]{"Search", "Food", "Coffee", "Shopping", "Fun", "Nightlife"};
    int flags[] = {R.drawable.search, R.drawable.food, R.drawable.icon, R.drawable.shopping, R.drawable.fun, R.drawable.nightlife};
    TextView txt;
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Log.d("state",String.valueOf(this));
        limit = 10;
        Spinner dropdown = (Spinner) findViewById(R.id.spinner1);
        searchButton = (Button) findViewById(R.id.button);
        txt = (TextView) findViewById(R.id.where);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), flags, items);
        ToSend = new SendToSecondActivity();
        dropdown.setAdapter(customAdapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0)
                    Category = (String) parent.getItemAtPosition(position);
                else
                    Category = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1234);
        }
        buildGoogleApiClient();
    }

    public void OnClickMain(View view) throws Exception {
        searchButton.setEnabled(false);
        PicCounter = 0;
        limit = 10;
        ParsedCity = "";
        City = ((EditText) findViewById(R.id.where)).getText().toString();
        City = new LanguageTranslation().execute(City).get(); // ---> this get sentence takes toooooo looooooooooong
        String[] result = City.split("\\s");
        int i = 0;
        for (; i < result.length - 1; i++)
            ParsedCity += result[i] + "%20";
        ParsedCity += result[i];
        BodyResult = new HttpRequest(ParsedCity, Category).execute(10).get();
        ToSend.ParsedObject = new Parser().execute(BodyResult).get();
        ToSend.City = City;
        ToSend.Category = Category;
        Intent myIntent = new Intent(this, SecondActivity.class);
        myIntent.putExtra("ParsedObject", (Serializable) ToSend);
        this.startActivity(myIntent);
        searchButton.setEnabled(true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationRequest = LocationRequest.create();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(10000);
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                lat = mLastLocation.getLatitude();
                lon = mLastLocation.getLongitude();
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                try {
                    if (gcd.getFromLocation(lat, lon, 1).size() > 0)
                        txt.setText(gcd.getFromLocation(lat, lon, 1).get(0).getLocality());
                    else {
                        if ((int) lat == 31 && (int) lon == 35)
                            txt.setText("Ramallah");
                        else if ((int) lat == 32 && (int) lon == 35)
                            txt.setText("Nablus");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("ERRORFindingLAtAndLong", e.getMessage() + " ");
                }
        } else
            Log.d("State", "Denied");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
}
