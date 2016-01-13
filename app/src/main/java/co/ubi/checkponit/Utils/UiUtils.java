package co.ubi.checkponit.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ${Mike} on 11/18/14.
 */
public class UiUtils {
    ProgressDialog progressDialog;
    Context context;

    public static String TAG = "UIUTILS";


    public UiUtils(Context context) {
        this.context = context;


    }


    public void showDialog(String text) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(text);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public void dissmisDialgog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();

    }

    public static void log(String TAG, String text) {
        Log.e(TAG, text);
    }

    public void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public boolean isGpsAvalible() {
        boolean result = false;
        isGpsTurnOn();
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            log(TAG, "GpsAvalible");
            result = true;


        } else {
            log(TAG, "NOGpsAvalible");
            result = false;

        }

        return result;
    }

    public static boolean isGpsAvalible(Context context) {
        boolean result = false;
        //isGpsTurnOn();
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.e(TAG, "GpsAvalible");
            result = true;


        } else {
            Log.e(TAG, "NOGpsAvalible");
            result = false;

        }

        return result;
    }

    public boolean isGpsTurnOn() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        lm.addGpsStatusListener(new android.location.GpsStatus.Listener() {
            public void onGpsStatusChanged(int event) {  // log(TAG,event+"");

                switch (event) {

                }
            }
        });

        return false;
    }


}
