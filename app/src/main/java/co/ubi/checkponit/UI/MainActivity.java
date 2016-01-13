package co.ubi.checkponit.UI;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;

import co.ubi.checkponit.DB.DBUtils;
import co.ubi.checkponit.R;
import co.ubi.checkponit.Services.Location.LocationService;
import co.ubi.checkponit.Services.Location.LocationUtils;
import co.ubi.checkponit.UI.Activitys.app_main_;
import co.ubi.checkponit.UI.Fragments.LoginFragment_;
import co.ubi.checkponit.Utils.PLayServicesUtils.PlayUtils;
import co.ubi.checkponit.Utils.ServerUtils.RESTCient;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {
    public static MainActivity mainActivity;
    public static final String TAG = "Main";
    // public static Fragment currentFragment;
    public static boolean isLoginFirst = false;
    public boolean loginVisibe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        //  DBUtils.initBBDD(this);
        DBUtils.getApplicationDataContext(this);
        Log.e(TAG, "onCreate");

        if (PlayUtils.checkPlayServices(this, false, this)) {
            if (RESTCient.isNetworkConnected(this)) {
                if (LocationUtils.isGpsAvalible(this)) {
                    //               goMap();
                    if (DBUtils.getApplicationDataContext(this).getUsuariosSet().getMyUser() != null) {
                        goMap();
                    }
//                    else if (currentFragment != null) {
//
//                        goCurrentFragment();
//                    }
                    else {
                        goLogin();
                    }

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

                .callback(new MaterialDialog.ButtonCallback() {
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
        Log.e(TAG, "goLogin");
        //   if (currentFragment != null && currentFragment.isVisible()) {
        //     Log.e(TAG, "currrentFragment isVisible");
        //   } else {
        loginVisibe = true;
        // currentFragment = new LoginFragment_();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(R.id.container, new LoginFragment_());
        //transaction.addToBackStack(null);
        transaction.commit();

        //  }


    }

    public void goMap() {
        Log.e(TAG, "goMap");
//        if (currentFragment != null && currentFragment.isVisible()) {
//            Log.e(TAG, "currrentFragment isVisible");
//        } else {
        //  currentFragment = new MapsFragment_();
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.add(R.id.container, new MapsFragment_());
//        //transaction.addToBackStack(null);
//        transaction.commit();
//        }
        Intent intent = new Intent(this, app_main_.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        //startActivity(new Intent(this, app_main_.class));

    }

//    public void goCurrentFragment() {
//        Log.e(TAG, "goCurrentFragment");
//        if (currentFragment != null && currentFragment.isVisible()) {
//            Log.e(TAG, "currrentFragment isVisible");
//        } else if (currentFragment != null) {
//            //currentFragment = new MapsFragment_();
//            FragmentManager manager = getSupportFragmentManager();
//            FragmentTransaction transaction = manager.beginTransaction();
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            transaction.add(R.id.container, currentFragment);
//            //transaction.addToBackStack(null);
//            transaction.commit();
//        }
//    }

    public void goFragment(Fragment fragment) {
        Log.e(TAG, "goFragment");
        //currentFragment = fragment;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(R.id.container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        Log.e(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        EventBus.getDefault().register(this);
        if (!LocationService.isServeiceRunning && !LocationService.isCapturingLocation) {
            startLocationService();
        }
    }

    public void startLocationService() {
        Intent msgIntent = new Intent(this, LocationService.class);
        msgIntent.setAction(LocationService.ACTION_START);
        startService(msgIntent);
        Log.e(TAG, "LocationService Start");
        if (DBUtils.getApplicationDataContext(this).getUsuariosSet().getMyUser() == null) {
            Log.e(TAG, "StartLocation GoLogin MAin");
            //goLogin();
        }
//        if(loginVisibe && isLoginFirst &&  currentFragment!=null && !currentFragment.isVisible() ){
//            goLogin();
//        }


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // EventBus.getDefault().post(true);
        new MaterialDialog.Builder(this)
                .title("Salir?")
                .content("Realmente deseas salir")
                .positiveText("Aceptar")
                .positiveColor(getResources().getColor(R.color.primary))
                .negativeText("Cancelar")
                .negativeColor(getResources().getColor(R.color.midnight_blue))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog materialDialog) {
                        Log.e(TAG, "onEvent BackPressed onNegative");
                    }

                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        finish();
                    }
                })
                .show();

    }

    public void onEvent(Fragment fragment) {
        Log.e(TAG, "onEvent GoFragment");
        goFragment(fragment);

    }


    public void onEvent(Boolean BackPressed) {
        Log.e(TAG, "onEvent BackPressed ");
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

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        //     currentFragment = null;
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult requestCode " + requestCode);
    }
}
