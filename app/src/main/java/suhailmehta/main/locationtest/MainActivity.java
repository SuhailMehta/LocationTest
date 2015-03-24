package suhailmehta.main.locationtest;

import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import suhailmehta.main.locationtest.location.LocationFinder;
import suhailmehta.main.locationtest.utils.Alerts;
import suhailmehta.main.locationtest.utils.NetworkStatus;


/**
 * Created by suhailmehta on 24/03/15.
 */
public class MainActivity extends ActionBarActivity {


    private LocationManager locationManager ;
    private TextView txLat,txLong ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        txLat = (TextView) findViewById(R.id.test_lat) ;
        txLong = (TextView) findViewById(R.id.test_long) ;

        findViewById(R.id.update_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUpdatedLocation();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getUpdatedLocation();

    }

    protected void getUpdatedLocation(){

        if(NetworkStatus.isConnected(this))
            if (locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER) && isHighAccuracyMode() ) {
                LocationFinder newLocationFinder = new LocationFinder();
                newLocationFinder.getLocation(this,
                        mLocationResult);
            } else
                Alerts.showGPSDisabledAlert(this);
        else
            Alerts.showNetworkNotAvailableAlert(this);

    }

    LocationFinder.LocationResult mLocationResult = new LocationFinder.LocationResult() {
        public void gotLocation(final double latitude, final double longitude) {
            if (latitude != 0.0 || longitude != 0.0)
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        txLat.setText("Lat: " + latitude);
                        txLong.setText("Lng: " + longitude);

                    }
                });
        }
    };

    private boolean isHighAccuracyMode(){

        int locationMode = -1 ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(),Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return (locationMode != Settings.Secure.LOCATION_MODE_OFF && locationMode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY); //check location mode

        }else{
            String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }

    }

}