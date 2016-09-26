package congybk.com.mapmvp.views.mapview.contract;


import java.util.List;

import congybk.com.mapmvp.models.objects.ResultMarker;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by YNC on 9/24/2016.
 */
public interface MapViewContract {
    void loadMap();
    void showError(String message);
    void showClickMarker();
    void showErrorLocation();
    void showErrorNetWork();
    void onMapLoaded();
    void loadMarkerSuccess(List<ResultMarker> resultMarkers);
}
