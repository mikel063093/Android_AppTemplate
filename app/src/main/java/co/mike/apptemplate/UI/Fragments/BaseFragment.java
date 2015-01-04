package co.mike.apptemplate.UI.Fragments;

/**
 * Created by ${Mike} on on ${DATE}.
 */
import android.app.ProgressDialog;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;

import co.mike.apptemplate.R;
import de.greenrobot.event.EventBus;


public class BaseFragment extends Fragment {
    ProgressDialog progressDialog;
    boolean isOnpause;


    public void onEvent(Location location) {

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


}