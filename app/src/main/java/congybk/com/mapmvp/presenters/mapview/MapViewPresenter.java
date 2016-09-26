package congybk.com.mapmvp.presenters.mapview;


import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import congybk.com.mapmvp.models.network.core.ApiClient;
import congybk.com.mapmvp.models.objects.ResultMarker;
import congybk.com.mapmvp.views.mapview.contract.MapViewContract;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by YNC on 9/24/2016.
 */
@EBean
public class MapViewPresenter implements GoogleMap.OnMapLoadedCallback, Callback<List<ResultMarker>> {
    private GoogleMap mMap;
    private LatLng mMyLatLng;
    private MapViewContract mMapViewContract;
    @RootContext
    Context mContext;

    public void init(MapViewContract mapViewContract) {
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

    public void loadLocation(Location location) {
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

    public void addListMarker(List<ResultMarker> resultMarkers) {
        for (ResultMarker marker : resultMarkers) {
            Log.i("TAG","Add Marker");
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
        Log.i("TAG","successs: "+resultMarkers.size());
        mMapViewContract.loadMarkerSuccess(resultMarkers);

    }

    @Override
    public void failure(RetrofitError error) {
        mMapViewContract.showError(error.getMessage());
    }
}
