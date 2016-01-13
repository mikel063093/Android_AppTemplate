package co.ubi.checkponit;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.support.multidex.MultiDex;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;

import co.ubi.checkponit.Services.Location.LocationService;
import co.ubi.checkponit.Services.Location.LocationUtils;

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
        initImageLoader(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                //.denyCacheImageMultipleSizesInMemory()

                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(100 * 1024 * 1024) // 50 Mb
              //  .tasksProcessingOrder(QueueProcessingType.FIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

        L.writeDebugLogs(true);
        L.writeLogs(true);

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
