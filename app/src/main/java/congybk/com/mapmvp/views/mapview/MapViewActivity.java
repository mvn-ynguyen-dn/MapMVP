package congybk.com.mapmvp.views.mapview;

import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import congybk.com.mapmvp.R;
import congybk.com.mapmvp.models.objects.ResultMarker;
import congybk.com.mapmvp.presenters.mapview.MapViewPresenter;
import congybk.com.mapmvp.views.BaseActivity;
import congybk.com.mapmvp.views.mapview.contact.MapContractView;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by YNC on 9/24/2016.
 */
@EActivity(R.layout.activity_map_view)
public class MapViewActivity extends BaseActivity implements MapContractView, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener {
    @ViewById(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bean
    MapViewPresenter mMapViewPresenter;

    private GoogleMap mMap;

    @Override
    protected void onStart() {
        super.onStart();
        mMapViewPresenter.connectLocation();
    }


    @Override
    protected void afterViews() {
        mMapViewPresenter.init(this);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mMapViewPresenter.clickMarker(marker);
        return false;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMapViewPresenter.loadMap();
        mMap = map;
        map.setOnMarkerClickListener(this);
        map.setOnCameraMoveListener(this);
    }

    @Override
    public void onCameraMove() {
        mMapViewPresenter.moveMap(mMap.getCameraPosition().target);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapViewPresenter.disConnectLocation();
    }

    @Override
    public void addMarker(ResultMarker marker) {
        double latitude = Double.parseDouble(marker.getLatitude());
        double longitude = Double.parseDouble(marker.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(marker.getName()));
    }

    @Override
    public void addMarkerLocation(ResultMarker marker) {
        double latitude = Double.parseDouble(marker.getLatitude());
        double longitude = Double.parseDouble(marker.getLongitude());
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(marker.getName()));
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
    public void moveLocation(LatLng location) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title("Marker"));
    }

    @Override
    public void loadFinish() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void loadStart() {
        mProgressBar.setVisibility(View.VISIBLE);
    }


}
