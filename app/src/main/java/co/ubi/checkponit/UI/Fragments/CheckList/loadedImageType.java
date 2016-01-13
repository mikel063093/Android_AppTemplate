package co.ubi.checkponit.UI.Fragments.CheckList;

import android.graphics.Bitmap;

/**
 * Created by ${Mike} on 1/16/15.
 */
public class loadedImageType {
    public static final int CHECK = 0;
    public static final int PROFILE = 1;
    Bitmap loadedImage;
    int type;

    public loadedImageType(Bitmap loadedImage, int type) {
        this.loadedImage = loadedImage;
        this.type = type;
    }

    public Bitmap getLoadedImage() {
        return loadedImage;
    }

    public int getType() {
        return type;
    }
}
