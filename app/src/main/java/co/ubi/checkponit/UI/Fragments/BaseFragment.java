package co.ubi.checkponit.UI.Fragments;

/**
 * Created by ${Mike} on on ${DATE}.
 */

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import java.util.ArrayList;

import co.ubi.checkponit.DB.Models.CheckList;
import co.ubi.checkponit.DB.Models.Push;
import co.ubi.checkponit.R;
import co.ubi.checkponit.UI.CustomEvent.LocationEvent;
import de.greenrobot.event.EventBus;


public class BaseFragment extends Fragment {
    ProgressDialog progressDialog;
    boolean isOnpause;
    static DisplayImageOptions options;

    public void onEvent(Push push) {

    }

    public void onEvent(Location location) {

    }

    public void onEvent(Boolean BackPressed) {

    }

    public void onEvent(ArrayList<CheckList> checkLists) {

    }

    public void onEvent(LocationEvent locationEvent) {

    }

    @Override
    public void onResume() {
        isOnpause = false;
        if (isStickyAvailable()) {
            EventBus.getDefault().registerSticky(this);
        } else {
            EventBus.getDefault().register(this);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        isOnpause = true;
        EventBus.getDefault().unregister(this);
        super.onPause();
    }


    protected boolean isStickyAvailable() {
        return false;
    }

    public void showDialog(String text) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(text);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void log(String TAG, String text) {
        Log.e(TAG, text);
    }

    public void goFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.container, fragment);
        // transaction.commit();

        if (isOnpause) {
            // isPendingDismiss = true;
        } else {
            transaction.commit();
            //isPendingDismiss = false;
        }
    }

    public void showMaterialDialogNeutral(String title, String content, String neutralText) {
        new MaterialDialog.Builder(getActivity())
                .title(title)
                .content(content)
                .neutralColor(getResources().getColor(R.color.accent))
                .neutralText(neutralText)
                .show();
    }

    static public DisplayImageOptions getOptionsImgCache() {
        if (options == null) {

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_launcher)
                    .showImageForEmptyUri(R.drawable.ic_launcher)
                    .showImageOnFail(R.drawable.ic_launcher)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .postProcessor(new BitmapProcessor() {
                        @Override
                        public Bitmap process(Bitmap bitmap) {

                            return Bitmap.createScaledBitmap(bitmap, 128, 128, true);
                        }
                    })
                    .displayer(new RoundedBitmapDisplayer(1000))
                            // .displayer(new RoundedBitmapDisplayer(20))
                    .build();


        }


        return options;
    }


}