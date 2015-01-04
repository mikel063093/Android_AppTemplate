package co.mike.apptemplate;

import android.app.Application;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import co.mike.apptemplate.Services.Location.LocationService;
import co.mike.apptemplate.Services.Location.LocationUtils;

/**
 * Created by ${Mike} on 1/3/15.
 */
public class AppTemplate extends Application {
    public static final String TAG = "AppTemplate";
    private Intent msgIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LocationUtils.isGpsAvalible(this)) {
            startLocationService();
        } else {
            Log.e(TAG, "NO GPS");
        }

    }


    public void startLocationService() {
        msgIntent = new Intent(this, LocationService.class);
        msgIntent.setAction(LocationService.ACTION_START);
        startService(msgIntent);
        Log.e(TAG, "LocationService Start");

    }
    public void onEvent(Location location) {
       // Log.e(TAG, "onEvente ... ");
    }
}
