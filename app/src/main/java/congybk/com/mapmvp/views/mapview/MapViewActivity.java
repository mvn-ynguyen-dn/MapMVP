package congybk.com.mapmvp.views.mapview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

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
public class MapViewActivity extends BaseActivity implements MapViewContract, OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {
    @ViewById(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bean
    MapViewPresenter mMapViewPresenter;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void init() {
        mMapViewPresenter.init(this);

    }

    @Override
    public void loadMap() {
        mProgressBar.setVisibility(View.VISIBLE);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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
        map.setOnMarkerClickListener(this);


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
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mMapViewPresenter.loadLocation(location);

    }

    @Override
    public void onConnectionSuspended(int i) {
        showLongToast(getString(R.string.location_connect_suspended));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showLongToast(getString(R.string.location_connect_faild));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mMapViewPresenter.clickMarker(marker);
        return false;
    }
}
