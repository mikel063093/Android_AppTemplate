package co.mike.apptemplate.UI;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;

import co.mike.apptemplate.R;
import co.mike.apptemplate.Services.Location.LocationService;
import co.mike.apptemplate.Services.Location.LocationUtils;
import co.mike.apptemplate.UI.Fragments.Login;
import co.mike.apptemplate.Utils.PLayServicesUtils.PlayUtils;
import co.mike.apptemplate.Utils.ServerUtils.RESTCient;

public class MainActivity extends ActionBarActivity {
    public static MainActivity mainActivity;
    public static final String TAG = "Main";
    public static Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        if (PlayUtils.checkPlayServices(this, false, this)) {
            if (RESTCient.isNetworkConnected(this)) {
                if (LocationUtils.isGpsAvalible(this)) {
                    goLogin();
                } else {
                    renderMeterialDialogNeutral(getString(R.string._error),
                            getString(R.string.no_gps),
                            getString(R.string.progress_button_acepted), 1);
                }
            } else {
                renderMeterialDialogNeutral(getString(R.string._error),
                        getString(R.string.interneterror),
                        getString(R.string.progress_button_acepted), 2);
            }
        } else {
            renderMeterialDialogNeutral(getString(R.string._error),
                    getString(R.string.play_services_no_avalible),
                    getString(R.string.progress_button_acepted), 0);
        }

    }

    public void renderMeterialDialogNeutral(String title, String content, String neutralText, final int type) {
        new MaterialDialog.Builder(this)
                .title(title)
                .content(content)
                .positiveColor(getResources().getColor(R.color.primary))
                .positiveText(neutralText)

                .callback(new MaterialDialog.Callback() {
                    @Override
                    public void onNegative(MaterialDialog materialDialog) {

                    }

                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        switch (type) {
                            case 0:
                                PlayUtils.checkPlayServices(mainActivity, true, mainActivity);
                                break;
                            case 1:
                                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                mainActivity.startActivity(intent);

                                break;
                            case 2:
                                mainActivity.finish();
                                break;

                        }

                    }

                })
                .show();
    }

    public void goLogin() {
        if (currentFragment != null && currentFragment.isVisible()) {
            Log.e(TAG, "currrentFragment isVisible");
        } else {
            currentFragment = new Login();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(R.id.container, currentFragment);
            //transaction.addToBackStack(null);
            transaction.commit();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        if (!isMyServiceRunning(LocationService.class)) {
            startLocationService();
        }
    }

    public void startLocationService() {
        Intent msgIntent = new Intent(this, LocationService.class);
        msgIntent.setAction(LocationService.ACTION_START);
        startService(msgIntent);
        Log.e(TAG, "LocationService Start");
        goLogin();

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
