package co.ubi.checkponit.UI.Fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.loopj.android.http.RequestParams;
import com.viewpagerindicator.CirclePageIndicator;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Pattern;

import co.ubi.checkponit.DB.DBUtils;
import co.ubi.checkponit.DB.Models.User;
import co.ubi.checkponit.FB.FBUtils;
import co.ubi.checkponit.FB.onFacebookListener;
import co.ubi.checkponit.R;
import co.ubi.checkponit.UI.Activitys.app_main_;
import co.ubi.checkponit.UI.Fragments.SliderTABS.sliderImgAdapter;
import co.ubi.checkponit.UI.MainActivity;
import co.ubi.checkponit.Utils.ServerUtils.RESTCient;
import co.ubi.checkponit.Utils.ServerUtils.URL;

/**
 * Created by ${Mike} on 1/3/15.
 */
@EFragment(R.layout.login_fragment)
public class LoginFragment extends BaseFragment implements onFacebookListener,
        RESTCient.onResponseServer {
    String possibleEmail;
    public static final String TAG = "LOGIN";
    FBUtils fbHelper;
    GraphUser user;
    Response response;

    sliderImgAdapter mAdapter;


    @ViewById
    CirclePageIndicator indicator;

    @ViewById
    ViewPager loginPagerSlider;

    @ViewById
    LoginButton authButton;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            log("STATS.CALLBACK", "OK");
            onSessionStateChange(session, state, exception);
        }
    };

    private UiLifecycleHelper uiHelper;

    //


    @Override
    public void onEvent(Location location) {
        super.onEvent(location);
        //  log(TAG, "onEvent Location :: " + location.toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);


    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getActivity()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
                log(TAG, "posibleEmail::" + possibleEmail);
            }
        }

        final float density = getResources().getDisplayMetrics().density;
        MainActivity.isLoginFirst = true;
        //indicator.setBackgroundColor(getResources().getColor(R.color.clouds));
        mAdapter = new sliderImgAdapter(getActivity().getSupportFragmentManager(), getActivity());
        indicator.setRadius(8 * density);
        indicator.setPageColor(getResources().getColor(R.color.primary));
        indicator.setFillColor(getResources().getColor(R.color.clouds));
        indicator.setStrokeColor(getResources().getColor(R.color.clouds));
        indicator.setStrokeWidth(2 * density);
//        ParallaxPagerTransformer pt = new ParallaxPagerTransformer((R.id.LinarLayoutSlideImageRoot));
//        pt.setSpeed(0.6f);
//        loginPagerSlider.setPageTransformer(false, pt);
        loginPagerSlider.setAdapter(mAdapter);

        if (loginPagerSlider != null) {
            log(TAG, "Indicator");

            indicator.setViewPager(loginPagerSlider);
            indicator.setSnap(true);

            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {

                    //Log.e(TAG, " position : " + position);
                }

                @Override
                public void onPageScrolled(int position,
                                           float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        } else {
            log(TAG, "No Indicator");
        }

        fbHelper = new FBUtils(this, getActivity());
        getKeyHash();
        authButton = (LoginButton) getActivity().findViewById(R.id.authButton);
        authButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));
        authButton.setFragment(this);
//        MainActivity.currentFragment = this;


    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }


    public void getKeyHash() {
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    "co.ubi.apptemplate",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                // textEmail.setText(Base64.encodeToString(md.digest(), Base64.DEFAULT));
               log("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    private void onSessionStateChange(Session session, SessionState state, Exception exception) {

        Log.d("onSessionStateChange", "OK");
        if (state.isOpened()) {
            log(TAG, "Logged in..." + " " + session.toString());
            //probamos si tenemos login

          log(TAG, "callgetDataFB");
            fbHelper.getDataFb(fbHelper.getSession());
            showDialog(getString(R.string.loading));
            //   DBUtils.initBBDD(getActivity());
            //  ui.showDialog( getString(R.string.loading));

        } else if (state.isClosed()) {
            log(TAG, "Logged out...");
        }
    }

    @Override
    public void OnProfilePictureDonwload(Bitmap phtoProfile, String url) {

    }

    @Override
    public void onProfileDataResponse(GraphUser user, Response response) {
        dismissDialog();
        if (user != null && response != null &&
                response.getGraphObject() != null &&
                response.getGraphObject().getProperty("email") != null) {
            this.user = user;
            this.response = response;
            /*
            * Send register
            * */
            RequestParams param = RESTCient.getParamsApiUser(null,
                    response.getGraphObject().getProperty("email").toString(),
                    null, user.getId());
            log(TAG, "intentamos loguear");
            new RESTCient(getActivity(), this, getActivity(), param, URL.LOGIN, RESTCient.TYPE_GCM_LOGIN);
            //RESTCient.setPostAsync(URL.REGISTER, RESTCient.TYPE_GCM_LOGIN, param, this);
            showDialog(getString(R.string.loading));

        } else {
            if (user != null && possibleEmail != null) {
                RequestParams param = RESTCient.getParamsApiUser(null,
                        possibleEmail,
                        null, user.getId());
                log(TAG, "intentamos loguear");
                new RESTCient(getActivity(), this, getActivity(), param, URL.LOGIN, RESTCient.TYPE_GCM_LOGIN);
                //RESTCient.setPostAsync(URL.REGISTER, RESTCient.TYPE_GCM_LOGIN, param, this);
                showDialog(getString(R.string.loading));

            } else {
                fbHelper.ClearCacheFB();
                Toast.makeText(getActivity(), "Debes aprovar el permiso de EMAIL", Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    public void onFriendsResponse(Response response) {

    }

    @Override
    public void OnResponse(String response, int satusCode, int type) {
        dismissDialog();
        fbHelper.ClearCacheFB();
        switch (satusCode) {
            case RESTCient.STATUS_CODE_OK:
                switch (type) {
                    case RESTCient.TYPE_GCM_REGISTER:
                        /*
                        * call UI
                        **/


                        User user = RESTCient.getUserFromJson(response);
                        if (user != null && DBUtils.saveUser(user, getActivity())) {
                            // EventBus.getDefault().post(new MapsFragment_());
                            Intent intent = new Intent(getActivity(), app_main_.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            log(TAG, ":(");
                        }

                        break;
                    case RESTCient.TYPE_GCM_LOGIN:
                        /*
                        * call main UI
                        * */
                        User user1 = RESTCient.getUserFromJson(response);
                        if (user1 != null && DBUtils.saveUser(user1, getActivity())) {
                            //  EventBus.getDefault().post(new MapsFragment_());
                            Intent intent = new Intent(getActivity(), app_main_.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            getActivity().finish();
                            //  startActivity(new Intent(getActivity(), app_main_.class));
                        } else {
                            log(TAG, ":(");
                        }

                        break;
                }
                break;
            case RESTCient.STATUS_CODE_ITERNAL_SERVER_ERROR:
                /*
                * call register
                * */
                setRegister();
                break;
            default:
                log(TAG, "Onresponse FAIL STATUS CODE :: " + satusCode + " TYPE " + type);
                break;
        }
    }


    public void setRegister() {
        if (user != null && response != null) {
            RequestParams
                    param = RESTCient.getParamsApiUser(user.getFirstName() + " " + user.getLastName(),
                    response.getGraphObject().getProperty("email").toString(),
                    getFbPhtoProfileUrl(user.getId()), user.getId());
            new RESTCient(getActivity(), this, getActivity(), param, URL.REGISTER, RESTCient.TYPE_GCM_REGISTER);

            showDialog(getString(R.string.loading));
        } else {
            log(TAG, "setRegister FAIL...");
            fbHelper.ClearCacheFB();

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fbHelper.ClearCacheFB();
        MainActivity.isLoginFirst = false;
    }

    public String getFbPhtoProfileUrl(String fb_id) {
        final String base = "https://graph.facebook.com/";
        String photo = base + fb_id + "/picture?type=large";

        return photo;
    }
}
