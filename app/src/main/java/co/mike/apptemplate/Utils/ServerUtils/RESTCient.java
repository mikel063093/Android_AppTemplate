package co.mike.apptemplate.Utils.ServerUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pkmmte.view.CircularImageView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;

import co.mike.apptemplate.R;
import co.mike.apptemplate.Utils.PLayServicesUtils.OnRegisterGcmListener;
import co.mike.apptemplate.Utils.PLayServicesUtils.PlayUtils;

/**
 * Created by ${Mike} on 12/28/14.
 */
public class RESTCient implements OnRegisterGcmListener {
    public static final int TYPE_GCM_LOGIN = 0;
    public static final int TYPE_GCM_REGISTER = 1;

    public static final int STATUS_CODE_OK = 200;
    public static final int STATUS_CODE_UNATHORIZED = 401;
    public static final int STATUS_CODE_PAYMENT_REQUIERED = 402;
    public static final int STATUS_CODE_FORBIDEN = 403;
    public static final int STATUS_CODE_CHECK_LICENSE = 930;
    public static final int STATUS_CODE_INVALID_LICENSE = 931;
    public static final int STATUS_CODE_SERVER_IN_MAINTENANCE = 0;
    public static final int STATUS_CODE_NOT_FOUND = 404;
    public static final int STATUS_CODE_ITERNAL_SERVER_ERROR = 500;
    public static final int STATUS_CODE_NO_IMPLEMENTED = 501;


    public static final String TAG = "SERVER";
    public static boolean isTaskPostRun = false;
    public static AsyncHttpClient client, fbcliet, postClient;
    private onResponseServer listener;
    public static Context context;
    public static Activity activity;
    private PlayUtils playUtils;
    private String URL;
    private int type;

    public static RequestParams params;

    public RESTCient(Context context,
                     onResponseServer l, Activity activity,
                     RequestParams user_params, String url, int type) {
        listener = l;
        this.context = context;

        this.activity = activity;
        this.playUtils = new PlayUtils(context, activity, this);
        if (playUtils.checkPlayServices()) {
            Log.e(TAG, "GCM REGISTER RUNNING");
            playUtils.registerInBackground();
        }
        this.params = user_params;
        this.URL = url;
        //usuario = new User();
        this.type = type;

        Log.e(TAG, "CONSTRUCT");
    }

    public static RequestParams getParamsRegisterUser(
            String name,
            String email,
            String photo,
            String city,
            String country,
            String info,
            String age,
            String gender,
            String facebook_id) {
        RequestParams user_params = new RequestParams();


        if (email != null) {
            user_params.put("email", email);
        }
        if (name != null) {
            user_params.put("name", name);
        }

        if (photo != null) {
            user_params.put("photo", photo);
        }
        if (city != null) {
            user_params.put("city", city);
        }
        if (country != null) {
            user_params.put("country", country);
        }

        if (info != null) {
            user_params.put("info", info);
        }

        if (facebook_id != null) {
            user_params.put("facebook_id", facebook_id);
        }
        if (age != null) {
            user_params.put("age", age);
        }
        if (gender != null) {
            user_params.put("gender", gender);
        }


//
//        if (user_params.get("notification_type") != null) {
//            usuario.setNotification_type(user_params.get("notification_type"));
//        }
//        if (user_params.get("photo") != null) {
//            usuario.setPhoto(user_params.get("photo"));
//        }
//        if (user_params.get("email") != null) {
//            usuario.setEmail(user_params.get("email"));
//        }
//        if (user_params.get("info") != null) {
//            usuario.setInfo(user_params.get("info"));
//
//        }
//        if (user_params.get("ccv_card") != null) {
//            usuario.setCcv_card(user_params.get("ccv_card"));
//        }
//        if (user_params.get("name") != null) {
//            usuario.setName(user_params.get("name"));
//        }
//        if (user_params.get("city") != null) {
//            usuario.setCity(user_params.get("city"));
//        }
//        if (user_params.get("country") != null) {
//            usuario.setCountry(user_params.get("country"));
//        }
//        if (user_params.get("card_name") != null) {
//            usuario.setCard_name(user_params.get("card_name"));
//        }
//        if (user_params.get("card_number") != null) {
//            usuario.setCard_number(user_params.get("card_number"));
//        }
//        if (user_params.get("expiration_date") != null) {
//            usuario.setExpiration_date(user_params.get("expiration_date"));
//        }
//        if (user_params.get("address") != null) {
//            usuario.setAddress(user_params.get("address"));
//        }
//        if (user_params.get("facebook_id") != null) {
//            usuario.setFacebookID(user_params.get("facebook_id"));
//        }
//        if (user_params.get("phone") != null) {
//            //usuario.setFacebookID(user_params.get("facebook_id"));
//        }

        return user_params;
    }

    public static RequestParams getParamsLoginUser(String user_id,
                                                   String latitude,
                                                   String longitude) {
        RequestParams paramsFb = new RequestParams();
        paramsFb.put("device", "ANDROID");
        paramsFb.put("user_id", user_id);
        paramsFb.put("latitude", latitude);
        paramsFb.put("longitude", longitude);

        return paramsFb;
    }

    public static String getRegisterIDFromJson(String json) {
        Log.e("RESPONSE :", json);
        String id = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            id = jsonObject.getString("id");
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
        return id;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    public static boolean isInternetAvailable(String domain) {
        try {
            InetAddress ipAddr = InetAddress.getByName(domain); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }

    public static void setPostAsync(String URL, final int TYPE, RequestParams params,
                                    final onResponseServer listener) {
        if (postClient != null && isTaskPostRun) {
            Log.e(TAG, "setPost isTaskRun ");
        } else {
            postClient = new AsyncHttpClient();

            AsyncHttpResponseHandler post = new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    Log.e(TAG, "onStart");
                    isTaskPostRun = true;
                }

                @Override
                public void onProgress(int bytesWritten, int totalSize) {
                    super.onProgress(bytesWritten, totalSize);
                    isTaskPostRun = true;
                    Log.e(TAG, "onProgress bytesWritten " + bytesWritten + "  totalSize " + totalSize);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    isTaskPostRun = false;
                    try {
                        String str = new String(responseBody, "UTF-8");
                        Log.e(TAG, "onSuccess statusCode " + statusCode + " responseBody " + str);
                        listener.OnResponse(str, statusCode, TYPE);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    try {
                        isTaskPostRun = false;
                        String str = new String(responseBody, "UTF-8");
                        Log.e(TAG, "onFailure statusCode " + statusCode + " responseBody " + str);
                        listener.OnResponse(str, statusCode, TYPE);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onRetry(int retryNo) {
                    super.onRetry(retryNo);
                    Log.e(TAG, "onRetry No " + retryNo);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    isTaskPostRun = false;
                    Log.e(TAG, "onFinish");
                }


            };

            postClient.post(URL, params, post);
        }


    }

    @Override
    public void OnRegegisterGcmResponse(String gcm_id) {

        if (gcm_id != null) {
            params.put("notification_api", gcm_id);
            params.put("notification_type", "GCM");
            //usuario.setNotification_api(gcm_id);
        }

        setPostAsync(URL, type, params, listener);

    }

    public interface onResponseServer {
        public void OnResponse(String response, int satusCode, int type);
    }

    public static void loadWebImage(final CircularImageView imgNetwork, String URL, Context context) {
        //final String URL = "http://i.imgur.com/LrwApXg.png";

        // Using Picasso...
        //Picasso.with(this).load(URL).placeholder(R.drawable.default_avatar).error(R.drawable.grumpy_cat).transform(new PicassoRoundTransform()).into(imgNetwork);

        // Using Glide...
        //Glide.with(this).load(URL).placeholder(R.drawable.default_avatar).error(R.drawable.grumpy_cat).into(imgNetwork);

        // Using ION...
        Ion.with(context)
                .load(URL)

                .asBitmap()

                .setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted(Exception e, Bitmap result) {
                        if (e == null) {
                            // Success
                            imgNetwork.setImageBitmap(result);
                        } else {
                            Log.e(TAG, "error downnload CAR URL");
                            imgNetwork.setImageResource(R.drawable.ic_launcher);
                        }
                    }
                });
    }

    public static void loadWebImage(final ImageView imgNetwork, String URL, Context context) {
        //final String URL = "http://i.imgur.com/LrwApXg.png";

        // Using Picasso...
        //Picasso.with(this).load(URL).placeholder(R.drawable.default_avatar).error(R.drawable.grumpy_cat).transform(new PicassoRoundTransform()).into(imgNetwork);

        // Using Glide...
        //Glide.with(this).load(URL).placeholder(R.drawable.default_avatar).error(R.drawable.grumpy_cat).into(imgNetwork);

        // Using ION...
        Ion.with(context)
                .load(URL)

                .asBitmap()

                .setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted(Exception e, Bitmap result) {
                        if (e == null) {
                            // Success
                            imgNetwork.setImageBitmap(result);
                        } else {
                            Log.e(TAG, "error downnload CAR URL");
                            imgNetwork.setImageResource(R.drawable.ic_launcher);
                        }
                    }
                });
    }


}
