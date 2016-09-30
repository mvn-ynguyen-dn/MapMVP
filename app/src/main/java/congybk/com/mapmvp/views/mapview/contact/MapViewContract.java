package congybk.com.mapmvp.views.mapview.contact;

import android.location.Location;

import java.util.List;

import congybk.com.mapmvp.models.objects.ResultMarker;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by YNC on 9/30/2016.
 */
public interface MapViewContract {

    void addMarker(ResultMarker marker);

    void addMarkerLocation(ResultMarker marker);

    void showError(String message);

    void showClickMarker();

    void showErrorLocation();

    void showErrorNetWork();

    void onMapLoaded();

    void moveLocation(Location location);

    void loadMarkerSuccess(List<ResultMarker> resultMarkers);
}
