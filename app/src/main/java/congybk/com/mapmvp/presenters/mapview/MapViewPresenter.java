package congybk.com.mapmvp.presenters.mapview;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

import congybk.com.mapmvp.models.network.core.ApiClient;
import congybk.com.mapmvp.models.objects.ResultMarker;
import congybk.com.mapmvp.views.mapview.contract.MapView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by YNC on 9/24/2016.
 */
@EBean
public class MapViewPresenter implements GoogleMap.OnMapLoadedCallback, Callback<List<ResultMarker>>
        , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    private LatLng mMyLatLng;
    private MapView mMapViewContract;
    private GoogleApiClient mGoogleApiClient;
    @RootContext
    Context mContext;

    public void init(MapView mapViewContract) {
        mMapViewContract = mapViewContract;
        mapViewContract.loadMap();
        ApiClient.call().getListMarker(this);
    }

    public void loadMap(GoogleMap map) {
        if (!isNetworkConnected()) {
            mMapViewContract.showErrorNetWork();
        }
        mMap = map;
        map.setOnMapLoadedCallback(this);
    }

    @Override
    public void onMapLoaded() {
        mMapViewContract.onMapLoaded();
    }

    public void setUpLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void addListMarker(List<ResultMarker> resultMarkers) {
        for (ResultMarker marker : resultMarkers) {
            double latitude = Double.parseDouble(marker.getLatitude());
            double longitude = Double.parseDouble(marker.getLongitude());
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(marker.getName()));
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        loadLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mMapViewContract.showErrorLocation();
    }

    public void connectLocation() {
        mGoogleApiClient.connect();
    }

    public void disConnectLocation() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void loadLocation() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            mMyLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMyLatLng, 16));
            mMap.addMarker(new MarkerOptions()
                    .position(mMyLatLng)
                    .title("ME"));

        } else {
            mMapViewContract.showErrorLocation();
        }
    }
}
