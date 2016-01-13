package co.ubi.checkponit.Services.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import co.ubi.checkponit.R;
import co.ubi.checkponit.UI.Activitys.app_main_;

/**
 * Created by ${Mike} on 12/15/14.
 */
public class Notification {
    public static final String Action_open_new = "open_new";
    NotificationCompat.Builder builder;
    int typeNototification;
    public static final int NOTIFICATION_ID = 1;
    String contentTitle, contentText, subText;
    Context context;
    String men;
    String action = null;
    Intent launchIntent;
    PendingIntent pendingIntent;


    public Notification(int typeNototification,
                        String contentTitle,
                        String contentText, String subText,
                        Context context) {
        this.typeNototification = typeNototification;
        this.contentTitle = contentTitle;
        this.contentText = contentText;
        this.subText = subText;
        this.context = context;
        builder = new NotificationCompat.Builder(context);
    }

    public Notification(int typeNototification,
                        String contentTitle,
                        String contentText, String subText,
                        Context context, String action) {
        this.typeNototification = typeNototification;
        this.contentTitle = contentTitle;
        this.contentText = contentText;
        this.subText = subText;
        this.context = context;
        this.action = action;
        builder = new NotificationCompat.Builder(context);
        // launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        launchIntent = new Intent(context, app_main_.class);
        if (action != null && launchIntent != null) {
            launchIntent.setAction(action);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        pendingIntent = PendingIntent.getActivity(context, -1, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);


    }




    public void sendNotification() {
        builder.setSmallIcon(R.drawable.ic_stat_noti);
        builder.setAutoCancel(true);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);

//        if(Constants.NOTIFICATION_CONDUCTOR==typeNototification){
//            builder.setAutoCancel(false);
//        }
//
//        else{
//            builder.setAutoCancel(true);
//
//        }
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_stat_noti));

        if (contentTitle != null)
            builder.setContentTitle(contentTitle);
        if (contentText != null)
            builder.setContentText(contentText);
        if (subText != null)
            builder.setSubText(subText);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
