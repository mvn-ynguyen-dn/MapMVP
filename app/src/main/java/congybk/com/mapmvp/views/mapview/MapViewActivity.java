package congybk.com.mapmvp.views.mapview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import congybk.com.mapmvp.R;
import congybk.com.mapmvp.models.objects.ResultMarker;
import congybk.com.mapmvp.presenters.mapview.MapViewPresenter;
import congybk.com.mapmvp.views.BaseActivity;
import congybk.com.mapmvp.views.mapview.contract.MapViewContract;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by YNC on 9/24/2016.
 */
@EActivity(R.layout.activity_map_view)
public class MapViewActivity extends BaseActivity implements MapViewContract, OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener {
    @ViewById(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bean
    MapViewPresenter mMapViewPresenter;

    GoogleMap mMap;

    @Override
    protected void init() {
        mMapViewPresenter.init(this);

    }

    @Override
    public void loadMap() {
        mProgressBar.setVisibility(View.VISIBLE);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        mMapViewPresenter.setUpLocation();
    }

    @Override
    public void showError(String message) {
        showLongToast(message);
    }

    @Override
    public void showClickMarker() {
        showLongToast(getString(R.string.show_click_marker));
    }

    @Override
    public void showErrorLocation() {
        showLongToast(getString(R.string.location_not_detected));
    }

    @Override
    public void showErrorNetWork() {
        showShortToast(getString(R.string.cannot_connect_internet));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMapViewPresenter.loadMap(map);
        mMap = map;
        map.setOnMarkerClickListener(this);
        map.setOnCameraMoveListener(this);
    }

    @Override
    public void onMapLoaded() {
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void loadMarkerSuccess(List<ResultMarker> resultMarkers) {
        mMapViewPresenter.addListMarker(resultMarkers);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mMapViewPresenter.connectLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapViewPresenter.disConnectLocation();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mMapViewPresenter.clickMarker(marker);
        return false;
    }

    @Override
    public void onCameraMove() {
        mMap.clear();
        LatLng latLng = mMap.getCameraPosition().target;
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Marker"));
    }
}
