package co.ubi.checkponit.UI.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.RequestParams;

import org.androidannotations.annotations.EFragment;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import co.ubi.checkponit.DB.DBUtils;
import co.ubi.checkponit.DB.Models.CheckList;
import co.ubi.checkponit.DB.Models.User;
import co.ubi.checkponit.R;
import co.ubi.checkponit.Services.Location.LocationService;
import co.ubi.checkponit.UI.CustomEvent.LocationEvent;
import co.ubi.checkponit.UI.Fragments.CheckList.CheckListAdapter;
import co.ubi.checkponit.Utils.ServerUtils.RESTCient;
import co.ubi.checkponit.Utils.ServerUtils.URL;
import co.ubi.checkponit.sothree.slidinguppanel.SlidingUpPanelLayout;
import de.greenrobot.event.EventBus;

//import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by ${Mike} on 1/7/15.
 */
@EFragment(R.layout.map_layout2)
public class MapsFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleMap.OnCameraChangeListener, SlidingUpPanelLayout.PanelSlideListener,
        RESTCient.onResponseServer {
    private static SupportMapFragment maPfragment;
    Bitmap Profilebitmap;
    public static final String TAG = "MapsFragment";
    Location myLocation;
    CheckListAdapter checkListAdapter;
    User user = null;
    GoogleMap mMap;
    public static Location cameraLocation;
    List<Marker> markerList;
    ArrayList<CheckList> mCheckLists = null;
//    View mTransparentHeaderView;
//
//    View mSpaceView;


    //@ViewById
//    ListView list;
//
//    @ViewById
//    SlidingUpPanelLayout slidingLayout;

//    @ViewById
//    Toolbar toolbar_map;
//
//    @ViewById
//    FloatingActionButton fab;
//
//    @ViewById
//    View transparentView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        log(TAG, "onActivityCreated");
        markerList = new ArrayList<>();

        user = DBUtils.getApplicationDataContext(getActivity()).getUsuariosSet().getMyUser();
        if (user != null && user.getPhoto() != null) {
            // Picasso.with(getActivity()).load(user.getPhoto()).into(userImgTarget);
        }

        initMap();

        getChekListFromServer();

    }


    public void initMap() {
        FragmentManager fm = getChildFragmentManager();
        maPfragment = (SupportMapFragment) fm.findFragmentById(R.id._map);
        if (maPfragment == null && mMap == null) {
            maPfragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id._map, maPfragment).commit();
            maPfragment.getMapAsync(this);
            log(TAG, "getMapAsync");


        } else {

            if (maPfragment.isVisible() && mMap != null) {
                log(TAG, "map is Visible");
                configMap();
            }

        }


    }

    @Override
    public void onMapReady(final GoogleMap mMap) {
        log(TAG, "onMapReady");
        this.mMap = mMap;
        configMap();
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setCompassEnabled(false);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        mMap.getUiSettings().setZoomControlsEnabled(false);
//        mMap.setOnCameraChangeListener(this);
//        final CameraUpdate update = getLastKnownLocation();
//        if (update != null) {
//            mMap.moveCamera(update);
//        }
//
//        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//            @Override
//            public boolean onMyLocationButtonClick() {
//                log(TAG, "MyLocationButtonClick");
//                LocationEvent event = new LocationEvent(null);
//                EventBus.getDefault().post(event);
//                mMap.moveCamera(update);
//                return true;
//            }
//        });
//        this.mMap = mMap;
//        if (mCheckLists != null && mCheckLists.size() > 0) {
//            showChecksOnMap(mCheckLists);
//        }

    }

    int conunt = 0;
    public static final int limit = 1;

    public void configMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setOnCameraChangeListener(this);
        final CameraUpdate update = getLastKnownLocation();
        conunt++;
        if (update != null && conunt == limit) {
            mMap.moveCamera(update);
        }

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                log(TAG, "MyLocationButtonClick");
                LocationEvent event = new LocationEvent(null);
                EventBus.getDefault().post(event);
                mMap.moveCamera(update);
                return true;
            }
        });

        if (mCheckLists != null && mCheckLists.size() > 0) {
            showChecksOnMap(mCheckLists);
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        cameraLocation = new Location("provider");
        cameraLocation.setLongitude(cameraPosition.target.longitude);
        cameraLocation.setLatitude(cameraPosition.target.latitude);
        EventBus.getDefault().post(new LocationEvent(cameraLocation));
        log(TAG, "onCameraChange");

    }

    @Override
    public void onEvent(Boolean BackPressed) {
        super.onEvent(BackPressed);
        log(TAG, "onEvent BackPressed");
        new MaterialDialog.Builder(getActivity())
                .title("Salir?")
                .content("Realmente deseas salir")
                .positiveText("Aceptar")
                .positiveColor(getResources().getColor(R.color.primary))
                .negativeText("Cancelar")
                .negativeColor(getResources().getColor(R.color.midnight_blue))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog materialDialog) {
                        log(TAG, "onEvent BackPressed onNegative");
                    }

                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        getActivity().finish();
                    }
                })
                .show();

    }

    @Override
    public void onPanelSlide(View view, float v) {

    }

    @Override
    public void onPanelCollapsed(View view) {

        //expandMap();
    }

    @Override
    public void onPanelExpanded(View view) {

    }

    @Override
    public void onPanelAnchored(View view) {

    }


    private CameraUpdate getLastKnownLocation() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        String provider = lm.getBestProvider(criteria, true);
        if (provider == null) {
            return null;
        }
        myLocation = lm.getLastKnownLocation(provider);
        Location loc = lm.getLastKnownLocation(provider);
        if (loc != null) {
            return CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 14.0f));
        }
        return null;
    }

    @Override
    public void onEvent(Location location) {
        super.onEvent(location);
        // log(TAG,"Location ::"+location.getProvider());
        myLocation = location;
    }

    @Override
    public void onEvent(LocationEvent locationEvent) {
        super.onEvent(locationEvent);
        cameraLocation = locationEvent.getCameraLocation();
    }


    @Override
    public void OnResponse(String response, int satusCode, int type) {
        dismissDialog();
        switch (satusCode) {
            case RESTCient.STATUS_CODE_OK:
                switch (type) {
                    case RESTCient.TYPE_CHECKPOINT:
                        break;
                    case RESTCient.TYPE_LIST_CHECKPOINT:
                        ArrayList<CheckList> checkLists = getCheckList(response);
                        if (checkLists != null) {
                            //  checkListAdapter = new CheckListAdapter(getActivity(), checkLists);
                            //list.setAdapter(checkListAdapter);
                        }

                        break;

                }
                break;
            case RESTCient.STATUS_CODE_ITERNAL_SERVER_ERROR:

                break;
            default:
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        initMap();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
        //   uploadReceiver.unregister(getActivity());
    }

    public Location getLastLocationDefault() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        String provider = lm.getBestProvider(criteria, true);
        if (provider == null) {
            return null;
        }
        myLocation = lm.getLastKnownLocation(provider);
        Location loc = lm.getLastKnownLocation(provider);
        if (loc != null) {
            return loc;
        } else {
            return null;
        }
    }

    public void getChekListFromServer() {
        if (myLocation == null) {
            myLocation = LocationService.getServiceLocation();
            if (myLocation == null) {
                myLocation = getLastLocationDefault();
            }
        }
        log(TAG, "getChekListFromServer");

        RequestParams params = new RequestParams();
        if (myLocation != null)
            params.put("lat_lng", myLocation.getLatitude() + "," + myLocation.getLongitude());

        showDialog(getString(R.string.loading));
        RESTCient.setPostAsync(URL.LIST_CHECKPOINT, RESTCient.TYPE_LIST_CHECKPOINT, params, this);
    }

    public ArrayList<CheckList> getCheckList(String response) {
        ArrayList<CheckList> checkLists = null;

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(response);
            checkLists = new ArrayList<>();
            //arrayList= new ArrayList<>();
            CheckList checkList;
            for (int i = 0; i < jsonArray.length(); i++) {

                checkList = RESTCient.getCheckListFromJson(jsonArray.getJSONObject(i));
                if (checkList != null) {
                    checkLists.add(checkList);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return checkLists;
    }

    @Override
    public void onEvent(ArrayList<CheckList> checkLists) {
        super.onEvent(checkLists);
        showChecksOnMap(checkLists);


        //setListAdapter(checkLists);
    }

    public void showChecksOnMap(ArrayList<CheckList> checkLists) {
        mCheckLists = checkLists;
        cleanMarkers();
        if (checkLists != null) {
            for (int i = 0; i < checkLists.size(); i++) {
                MarkerOptions marker = new MarkerOptions();
                marker.draggable(false);
                if (checkLists.get(i).GetLatLng(checkLists.get(i).getLat_lat()) != null) {
                    marker.position(checkLists.get(i).GetLatLng(checkLists.get(i).getLat_lat()));
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_beenhere_black_48dp));
                    if (checkLists.get(i).getDescription() != null && !checkLists.get(i).getDescription().equals("null")) {
                        marker.snippet(checkLists.get(i).getDescription());
                        marker.title(checkLists.get(i).getDescription());

                    }
                    if (mMap != null) {
                        markerList.add(mMap.addMarker(marker));
                    }
                }


            }
        }
    }

    public void cleanMarkers() {
        if (markerList != null && markerList.size() > 0) {
            for (int i = 0; i < markerList.size(); i++) {
                markerList.get(i).remove();
            }
        }
    }


}
