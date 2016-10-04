package congybk.com.mapmvp.presenters.mapview;


import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;

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
public class MapViewPresenter implements Callback<List<ResultMarker>>
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

    public void loadMap() {
        if (!checkNetworkConnected()) {
            mMapViewContract.showErrorNetWork();
        }
    }

    private boolean checkNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    public void clickMarker(Marker marker) {
        if (mMyLatLng.equals(marker.getPosition())) {
            mMapViewContract.showClickMarker();
        }
    }

    public void connectLocation() {
        mMapViewContract.loadStart();
        mLocationTracker.connectLocation();
    }

    public void disConnectLocation() {
        mLocationTracker.disconnectLocation();
    }

    public void moveMap(LatLng latLng) {
        mMapViewContract.moveLocation(latLng);
    }

    /*request api*/
    @Override
    public void success(List<ResultMarker> resultMarkers, Response response) {
        for (ResultMarker marker : resultMarkers) {
            mMapViewContract.addMarker(marker);
        }
    }

    @Override
    public void failure(RetrofitError error) {
        mMapViewContract.showError(error.getMessage());
    }

    /*search location*/
    @Override
    public void loadLocationError() {
        mMapViewContract.showErrorLocation();
        mMapViewContract.loadFinish();
    }

    @Override
    public void loadLocationSucces(Location location) {
        mMyLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMapViewContract.loadFinish();
        mMapViewContract.addMarkerLocation(new ResultMarker("ME", String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())));
    }

}
