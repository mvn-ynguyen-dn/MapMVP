package congybk.com.mapmvp.presenters.mapview;


import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

import congybk.com.mapmvp.models.location.LocationTracker;
import congybk.com.mapmvp.models.network.core.ApiClient;
import congybk.com.mapmvp.models.objects.ResultMarker;
import congybk.com.mapmvp.presenters.mapview.contact.MapViewPreseterContact;
import congybk.com.mapmvp.views.mapview.contact.MapContractView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by YNC on 9/24/2016.
 */
@EBean
public class MapViewPresenter implements GoogleMap.OnMapLoadedCallback, Callback<List<ResultMarker>>
        , MapViewPreseterContact {
    private LatLng mMyLatLng;
    private MapContractView mMapViewContract;
    @RootContext
    Context mContext;

    @Bean
    LocationTracker mLocationTracker;

    public void init(MapContractView mapViewContract) {
        mMapViewContract = mapViewContract;
        mLocationTracker.init(this);
        ApiClient.call().getListMarker(this);
    }

    public void loadMap(GoogleMap map) {
        if (!isNetworkConnected()) {
            mMapViewContract.showErrorNetWork();
        }
        map.setOnMapLoadedCallback(this);
    }

    @Override
    public void onMapLoaded() {
        mMapViewContract.onMapLoaded();
    }

    public void addListMarker(List<ResultMarker> resultMarkers) {
        for (ResultMarker marker : resultMarkers) {
            mMapViewContract.addMarker(marker);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    public void clickMarker(Marker marker) {
        if (mMyLatLng.equals(marker.getPosition())) {
            mMapViewContract.showClickMarker();
        }
    }

    @Override
    public void success(List<ResultMarker> resultMarkers, Response response) {
        mMapViewContract.loadMarkerSuccess(resultMarkers);

    }

    @Override
    public void failure(RetrofitError error) {
        mMapViewContract.showError(error.getMessage());
    }


    public void connectLocation() {
        mLocationTracker.connectLocation();
    }

    public void disConnectLocation() {
        mLocationTracker.disconnectLocation();
    }

    @Override
    public void loadLocationError() {
        mMapViewContract.showErrorLocation();
    }

    @Override
    public void loadLocationSucces(Location location) {
        mMyLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMapViewContract.addMarkerLocation(new ResultMarker("ME", String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())));
    }

}
