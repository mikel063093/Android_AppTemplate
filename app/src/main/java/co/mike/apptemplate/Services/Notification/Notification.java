package co.mike.apptemplate.Services.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import co.mike.apptemplate.R;
import co.mike.apptemplate.UI.MainActivity;

/**
 * Created by ${Mike} on 12/15/14.
 */
public class Notification {
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
        launchIntent = new Intent(context, MainActivity.class);
        if (action != null && launchIntent != null) {
            launchIntent.setAction(action);
        }
        pendingIntent = PendingIntent.getActivity(context, -1, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);


    }




    public void sendNotification() {
        builder.setSmallIcon(R.drawable.ic_launcher);

//        if(Constants.NOTIFICATION_CONDUCTOR==typeNototification){
//            builder.setAutoCancel(false);
//        }
//
//        else{
//            builder.setAutoCancel(true);
//
//        }
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));

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
