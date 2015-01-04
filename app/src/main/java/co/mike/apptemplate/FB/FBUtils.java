package co.mike.apptemplate.FB;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mike on 29/09/2014.
 */
public class FBUtils {

    public onFacebookListener listener;
    public static final String TAG = "FBUtils";
    Context context;

    public FBUtils(onFacebookListener listener, Context context) {
        this.listener = listener;
        this.context = context;

        Log.e(TAG, "Construct");
    }


    public final void getGraphApiPhotoUrl(Session session, Bundle params) {

        new Request(session, "/me/picture", params, HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(Response response) {
                        JSONObject fbDataObj;
                        if (response != null) {
                            // Log.d("FB RESPONSE", response.toString());
                            try {
                                fbDataObj = response.getGraphObject()
                                        .getInnerJSONObject().getJSONObject("data");

                                // Log.d("FB RESPONSE", fbDataObj.get("url")
                                //.toString());

                                asyncDownloadImg(fbDataObj.get("url").toString());


                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        } else {

                        }


                    }
                }
        ).executeAsync();

    }

    public final void asyncDownloadImg(final String url) {
//        ImageRequest ir = new ImageRequest(url, new com.android.volley.Response.Listener<Bitmap>() {
//            @Override
//            public void onResponse(Bitmap response) {
//                if (listener != null) {
//                  //  Log.e(TAG, "DATA URL:" + url);
//                    listener.OnProfilePictureDonwload(response, url);
//                } else {
//                   // Log.e(TAG, " PHOTO listener null");
//                }
//            }
//        }, 160, 160, null, null);
//        cola.add(ir);

    }


    public Bundle getParamsPhotoProfile() {
        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        params.putString("type", "square");
        params.putString("width", "160");
        params.putString("height", "160");
        return params;
    }

    public Session getSession() {
        Session session = Session.getActiveSession();
        if (session != null) {
            if (!session.isClosed()) {


                Log.e("SessionFb", "closed");
            } else {
                Log.e("SessionFb", "NOclosed");
            }
        } else {
            session = null;
            session = Session.openActiveSessionFromCache(context);
            if (session != null && session.isOpened()) {

                Log.e("SessionFb", "OPEN");
            } else {
                Log.e("SessionFb", "CLOSED OR NULL");
            }
        }
        return session;
    }

    public void getDataFb(final Session session) {

        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(final GraphUser user, Response response) {
                        if (session == Session.getActiveSession()) if (user != null) {
                            //     Log.d("DataFB", user.getName());

                            //Map<String, String> paramsFb = ServerUtils.getParamsFB(response.getGraphObject().getProperty("email").toString(),
                            //      "facebook", user.getName(), user.getId());
                            //Toast.makeText(getActivity(),session.getAccessToken().toString(),Toast.LENGTH_SHORT).show();
                            Bundle params = new Bundle();
                            params.putString("fields", "id, name, picture, email, bio, gender, age_range");
                            new Request(
                                    session,
                                    "/me",
                                    params,
                                    HttpMethod.GET,
                                    new Request.Callback() {
                                        public void onCompleted(Response respons) {
                                            Log.e("dataRespos", respons.toString());

                                            if (listener != null) {
                                                Log.e(TAG, "DATA " + respons.toString());
                                                listener.onProfileDataResponse(user, respons);
                                            } else {
                                                Log.e(TAG, "DATA listener null");
                                            }


                                        }
                                    }
                            ).executeAsync();

                        }

                    }
                }
        );
        request.executeAsync();

    }

    public void ClearCacheFB() {
        Session session = getSession();
        if (session != null) {
            session.closeAndClearTokenInformation();
        }

    }

    public void getAppFriends(Session session) {
        Bundle params = new Bundle();
        params.putString("fields", "id, name, picture");
        new Request(
                session,
                "/me/friends",
                params,
                HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(Response response) {
                        Log.e(TAG, response.toString());
                        if (listener != null) {
                            listener.onFriendsResponse(response);
                        }
                    }
                }
        ).executeAsync();

    }

}
