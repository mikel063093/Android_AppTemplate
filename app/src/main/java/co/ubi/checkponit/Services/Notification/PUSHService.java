package co.ubi.checkponit.Services.Notification;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import co.ubi.checkponit.DB.Models.Push;
import de.greenrobot.event.EventBus;


/**
 * Created by ubi on 15/09/2014.
 */
public class PUSHService extends IntentService {
    private Context context;
    public static volatile int startId;


    public static final String TAG = "PUSHService";

    /*
    * REPLACE WHIT YOU KEY FROM SERVER *
    * */
    public static final String GCM_KEY_SERVER = "msg";

    public void onEvent(Object event) {

        Log.e(TAG, "onEvent ");
    }

    public void onEvent(Push push) {

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
                Log.e(TAG, extras.toString());
                Push push = getPushFromJson(extras.getString(GCM_KEY_SERVER));
                if (push != null) {


                    if (!push.getDescription().equals("null") && !push.getAddress().equals("null")) {
                        Notification notification = new Notification(1, push.getName(),
                                push.getDescription(),
                                push.getAddress(), context, Notification.Action_open_new);
                        notification.sendNotification();

                    } else {
                        Notification notification = new Notification(1, push.getName(),
                                "",
                                "", context, Notification.Action_open_new);
                        notification.sendNotification();
                        //  EventBus.getDefault().post(push);
                    }
                    EventBus.getDefault().post(push);
                }
                //tittle
                /***
                 * tittle
                 * name
                 * photo
                 * photo_check
                 * lat_lng
                 *
                 * **/

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
        if (!EventBus.getDefault().isRegistered(this)) {

            EventBus.getDefault().register(this);
        } else {
            Log.e(TAG, "EventBus now Registerd");
        }


        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public Push getPushFromJson(String response) {
        Push push = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            push = new Push(
                    jsonObject.getString(Push.KEY_title),
                    jsonObject.getString(Push.KEY_name),
                    jsonObject.getString(Push.KEY_photo),
                    jsonObject.getString(Push.KEY_photo_check),
                    jsonObject.getString(Push.KEY_lat_lng),
                    jsonObject.getString(Push.KEY_address),
                    jsonObject.getString(Push.KEY_description)
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return push;
    }
}