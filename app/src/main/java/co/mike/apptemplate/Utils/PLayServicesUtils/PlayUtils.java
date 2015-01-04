package co.mike.apptemplate.Utils.PLayServicesUtils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import co.mike.apptemplate.R;



/**
 * Created by mike on 15/09/2014.
 */
public class PlayUtils {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String TAG = "PlayUtils";
    GoogleCloudMessaging gcm;
    String regid;
    Context context;
    Activity activity;
    OnRegisterGcmListener listener;
    int type;

    public PlayUtils(Context context, Activity activity, OnRegisterGcmListener l) {
        this.context = context;
        this.activity = activity;
        this.listener = l;

    }

    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    public static boolean checkPlayServices(Context context, boolean show, Activity activity) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                if (show) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
                }
            } else {
                Log.e(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }


    public void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(context.getString(R.string.SENDERID));
                    msg = "Device registered, registration ID=" + regid;
                    listener.OnRegegisterGcmResponse(regid);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    listener.OnRegegisterGcmResponse(null);
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG, msg);
            }
        }.execute(null, null, null);
    }
}
