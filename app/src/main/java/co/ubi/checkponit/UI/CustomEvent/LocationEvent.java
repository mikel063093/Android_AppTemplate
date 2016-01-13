package co.ubi.checkponit.UI.CustomEvent;

import android.location.Location;

/**
 * Created by ${Mike} on 1/19/15.
 */
public class LocationEvent {
    Location CameraLocation;

    public LocationEvent(Location cameraLocation) {
        CameraLocation = cameraLocation;
    }

    public Location getCameraLocation() {
        return CameraLocation;
    }
}
