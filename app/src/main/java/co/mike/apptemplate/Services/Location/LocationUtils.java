package co.mike.apptemplate.Services.Location;

import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by ${Mike} on 1/3/15.
 */
public class LocationUtils {
    public static final String TAG = "LocationUtils";

    public static boolean isGpsAvalible(Context context) {
        boolean result = false;
        //isGpsTurnOn();
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||

                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.e(TAG, "GpsAvalible");
            result = true;


        } else {
            Log.e(TAG, "NOGpsAvalible");
            result = false;

        }

        return result;
    }
}
