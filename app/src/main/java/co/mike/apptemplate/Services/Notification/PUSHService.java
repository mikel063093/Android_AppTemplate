package co.mike.apptemplate.Services.Notification;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;



import de.greenrobot.event.EventBus;


/**
 * Created by mike on 15/09/2014.
 */
public class PUSHService extends IntentService {
    private Context context;
    public static volatile int startId;


    public static final String TAG = "PUSHService";

    /*
    * REPLACE WHIT YOU KEY FROM SERVER *
    * */
    public static final String GCM_KEY_SERVER = "MSG";

    public void onEvent(Object event) {

        Log.e(TAG, "onEvent ");
    }

    public PUSHService() {
        super("PUSHService");
        this.context = this;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.e(TAG, extras.getString(GCM_KEY_SERVER));
                Notification notification;
                /**
                 * TODO:
                 *
                 * what you need to do with your GCM, display a notification or
                 * update UI.
                 * I recommend using EventBus.getDefault().post (CustomObeject object);
                 * to send messages or objects to the UI (Fragments or Activity).
                 *
                 * **/


            }

        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startId = startId;
        EventBus.getDefault().register(this);

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}