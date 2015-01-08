package co.mike.apptemplate.UI.Fragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.loopj.android.http.RequestParams;
import com.viewpagerindicator.CirclePageIndicator;
import com.xgc1986.parallaxPagerTransformer.ParallaxPagerTransformer;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import co.mike.apptemplate.FB.FBUtils;
import co.mike.apptemplate.FB.onFacebookListener;
import co.mike.apptemplate.R;
import co.mike.apptemplate.UI.Fragments.SliderTABS.sliderImgAdapter;
import co.mike.apptemplate.Utils.ServerUtils.RESTCient;
import co.mike.apptemplate.Utils.ServerUtils.URL;

/**
 * Created by ${Mike} on 1/3/15.
 */
@EFragment(R.layout.login_fragment)
public class LoginFragment extends BaseFragment implements onFacebookListener,
        RESTCient.onResponseServer {

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
        log(TAG, "onEvent Location :: " + location.toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final float density = getResources().getDisplayMetrics().density;
        //indicator.setBackgroundColor(getResources().getColor(R.color.clouds));
        mAdapter = new sliderImgAdapter(getActivity().getSupportFragmentManager(), getActivity());
        indicator.setRadius(8 * density);
        indicator.setPageColor(getResources().getColor(R.color.primary));
        indicator.setFillColor(getResources().getColor(R.color.clouds));
        indicator.setStrokeColor(getResources().getColor(R.color.clouds));
        indicator.setStrokeWidth(2 * density);
        ParallaxPagerTransformer pt = new ParallaxPagerTransformer((R.id.LinarLayoutSlideImageRoot));
        pt.setSpeed(0.6f);
        loginPagerSlider.setPageTransformer(false, pt);
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
                    "co.mike.apptemplate",
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
        if (user != null && response != null) {
            this.user = user;
            this.response = response;
            /*
            * Send register
            * */
            RequestParams param = RESTCient.getParamsRegisterUser(null,
                    response.getGraphObject().getProperty("email").toString(),
                    null, null, null, null, null, null, null
            );
            RESTCient.setPostAsync(URL.REGISTER, RESTCient.TYPE_GCM_REGISTER, param, this);
            showDialog(getString(R.string.loading));

        } else {

        }

    }

    @Override
    public void onFriendsResponse(Response response) {

    }

    @Override
    public void OnResponse(String response, int satusCode, int type) {
        dismissDialog();
        switch (satusCode) {
            case RESTCient.STATUS_CODE_OK:
                switch (type) {
                    case RESTCient.TYPE_GCM_REGISTER:
                        /*
                        * call loading :)
                        **/


                        break;
                    case RESTCient.TYPE_GCM_LOGIN:
                        /*
                        * call main UI
                        * */
                        break;
                }
                break;
            case RESTCient.STATUS_CODE_ITERNAL_SERVER_ERROR:
                /*
                * call register
                * */
                setRegister();
                break;
        }
    }


    public void setRegister() {
        if (user != null && response != null) {
            RequestParams param = RESTCient.getParamsRegisterUser(
                    user.getFirstName() + " " + user.getLastName(),
                    response.getGraphObject().getProperty("email").toString(),
                    null, null, null, null,
                    response.getGraphObject().getProperty("age_range").toString(),
                    response.getGraphObject().getProperty("gender").toString(),
                    user.getId()
            );
            RESTCient.setPostAsync(URL.REGISTER, RESTCient.TYPE_GCM_REGISTER, param, this);
            showDialog(getString(R.string.loading));
        } else {

        }
    }
}
