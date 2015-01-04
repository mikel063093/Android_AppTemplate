package co.mike.apptemplate.FB;

import android.graphics.Bitmap;

import com.facebook.Response;
import com.facebook.model.GraphUser;

/**
 * Created by mike on 29/09/2014.
 */
public interface onFacebookListener {
    void OnProfilePictureDonwload(Bitmap phtoProfile, String url);

    void onProfileDataResponse(GraphUser user, Response response);
    void onFriendsResponse(Response response);
}
