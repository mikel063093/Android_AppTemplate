package co.mike.apptemplate.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;

import org.androidannotations.annotations.EFragment;

import co.mike.apptemplate.R;

/**
 * Created by ${Mike} on 1/7/15.
 */
@EFragment(R.layout.map_layout)
public class MapsFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener {
    private static SupportMapFragment maPfragment;
    public static final String TAG = "MapsFragment";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        log(TAG, "onActivityCreated");
        initMap();

    }

    public void initMap() {
        FragmentManager fm = getChildFragmentManager();
        maPfragment = (SupportMapFragment) fm.findFragmentById(R.id._map);
        if (maPfragment == null) {
            maPfragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id._map, maPfragment).commit();
            maPfragment.getMapAsync(this);
            log(TAG, "getMapAsync");


        } else {

            if (maPfragment.isVisible()) {
                log(TAG, "map is Visible");
            }

        }


//        MapFragment mapFragment = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        log(TAG,"onMapReady");
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setOnCameraChangeListener(this);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                log(TAG, "MyLocationButtonClick");

                return true;
            }
        });
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }
}
