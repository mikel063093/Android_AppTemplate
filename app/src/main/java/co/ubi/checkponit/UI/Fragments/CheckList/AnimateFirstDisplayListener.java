package co.ubi.checkponit.UI.Fragments.CheckList;

/**
 * Created by ${Mike} on 1/15/15.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pkmmte.view.CircularImageView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * Created by ${Mike} on 11/26/14.
 */
public class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
    int type = -1;
    public static final int TYPE_CIRLCE = 1;
    public static final int TYPE_GRID = 2;
    public static final int TYPE_LIST = 3;
    public static final int TYPE_CIRCLE_IMAGE = 4;
    public static final int TYPE_CIRCLE = 5;
    public static CircularImageView circularImageView;
    public Context context;
    int imgType = -1;

    public AnimateFirstDisplayListener() {
        registerdEventBus();

    }

    public void registerdEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            Log.e(TAG, "RegisteredEventBus");
        } else {
            Log.e(TAG, "IS NOW RegisteredEventBus");
        }
    }

    public void unRegistredEventBUs() {
        EventBus.getDefault().unregister(this);
        Log.e(TAG, "unRegisterEventBus");
    }

    public static void setCircularImageView(CircularImageView circularImageView) {

        AnimateFirstDisplayListener.circularImageView = circularImageView;
    }

    public AnimateFirstDisplayListener(int type, CircularImageView circularImageView, Context context) {
        this.type = type;
        this.circularImageView = circularImageView;
        this.context = context;
        registerdEventBus();
    }

    public AnimateFirstDisplayListener(int type) {
        registerdEventBus();
        this.type = type;

    }

    public static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
    public static String TAG = "AnimateDListener";

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        Log.e(TAG, "onLoadingComplete TYPE " + type);
        if (loadedImage != null) {

            boolean firstDisplay = !displayedImages.contains(imageUri);
            // imageView.setBackground(loadedImage);


            if (firstDisplay) {
                ImageView imageView = (ImageView) view;
                imageView.setImageBitmap(loadedImage);
                // Log.e(TAG,"firstDisplay");
                // FadeInBitmapDisplayer.animate(imageView, 500);
                displayedImages.add(imageUri);
            } else {
                // Log.e(TAG,"size array "+displayedImages.size());
                // Log.e(TAG," NO firstDisplay");
            }
        }
    }

    public void onEvent(loadedImageType img) {
        // Log.e(TAG, "onEvent imgType");
        this.imgType = img.getType();
    }

    public void setPictureProfile(Bitmap ImageProfile, ImageView profile) {
        if (profile != null) {

            Bitmap criculo = Bitmap.createBitmap(ImageProfile.getWidth(), ImageProfile.getHeight(), Bitmap.Config.ARGB_8888);
            BitmapShader shader = new BitmapShader(ImageProfile, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setShader(shader);
            Canvas c = new Canvas(criculo);
            c.drawCircle(ImageProfile.getWidth() / 2, ImageProfile.getHeight() / 2, ImageProfile.getWidth() / 2, paint);
            profile.setImageBitmap(criculo);

        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
        //return bm;
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {
        super.onLoadingStarted(imageUri, view);
        Log.e(TAG, "onLoadignStarted URL " + imageUri);
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        super.onLoadingFailed(imageUri, view, failReason);
        // unRegistredEventBUs();
        Log.e(TAG, "onLoadingFaild " + failReason.toString(), failReason.getCause());
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {
        super.onLoadingCancelled(imageUri, view);
        Log.e(TAG, "onLoadingCancel " + imageUri);
        // unRegistredEventBUs();
    }

}