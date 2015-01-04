package co.mike.apptemplate.Services.Location;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import de.greenrobot.event.EventBus;


/**
 * An {@link android.app.IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class LocationService extends IntentService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    public static final String ACTION_START = "start";
    public static final String ACTION_STOP = "stop";
    public static final String TAG = "LocationService";
    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;
    public static boolean isCapturingLocation = false;

    public static volatile Boolean OFF = false;
    public static volatile int startId;

    public static int PRIORITY = 0;
    public static boolean isServiceActive = false;

    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static int UPDATE_INTERVAL_IN_SECONDS = 10;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
            * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static int FASTEST_INTERVAL_IN_SECONDS = 7;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND
            * FASTEST_INTERVAL_IN_SECONDS;
    public static Location serviceLocation;



    public LocationService() {
        super("LocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.e(TAG, action);
            if (ACTION_START.equals(action)) {
                OFF = false;

                startLocation();

            } else if (ACTION_STOP.equals(action)) {

                Log.d("StopSelf", "startID :" + startId);
                stopSelf(startId);
                stopSelf();
                stopLocation();
            }
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int StartId) {
        startId = StartId;
        Log.d("OnStartComand", "startID :" + startId);
        if (EventBus.getDefault().isRegistered(getApplicationContext())) {
            Log.e(TAG, "onEvent registred");
        } else {
            EventBus.getDefault().register(getApplicationContext());
        }


        return super.onStartCommand(intent, flags, StartId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Log.e(TAG, "oDestroy");
    }

    public void startLocation() {

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() ||
                mGoogleApiClient != null && mGoogleApiClient.isConnecting()
                ) {
            Log.e(TAG, "mGoogleApiCLient is contected now or concting");
        } else {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();
        }


    }

    public void stopLocation() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.unregisterConnectionCallbacks(this);
            mGoogleApiClient.unregisterConnectionFailedListener(this);
            mGoogleApiClient.disconnect();
        }


        // stopSelf(startId);
        //stopSelf();


    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        if (getPriority() != 0) {
            mLocationRequest.setPriority(getPriority());
        } else {
            mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        }
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setInterval(UPDATE_INTERVAL); // Update location every second


        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    public int getPriority() {
        return PRIORITY;
    }

    public static void setPRIORITY(int PRIORITY) {
        LocationService.PRIORITY = PRIORITY;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onLocationChanged(Location location) {
        isCapturingLocation = true;
        serviceLocation = location;
        //Log.e("onLocationChanged SERVICE UI ", location.toString());
        EventBus.getDefault().post(location);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "GoogleApiClient connection has failed");
    }


}
