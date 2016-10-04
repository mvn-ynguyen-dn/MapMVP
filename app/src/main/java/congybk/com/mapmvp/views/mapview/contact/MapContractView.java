package congybk.com.mapmvp.views.mapview.contact;

import com.google.android.gms.maps.model.LatLng;

import congybk.com.mapmvp.models.objects.ResultMarker;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by YNC on 9/30/2016.
 */
public interface MapContractView {

    void addMarker(ResultMarker marker);

    void addMarkerLocation(ResultMarker marker);

    void showError(String message);

    void showClickMarker();

    void showErrorLocation();

    void showErrorNetWork();

    void moveLocation(LatLng location);

    void loadFinish();

    void loadStart();
}
