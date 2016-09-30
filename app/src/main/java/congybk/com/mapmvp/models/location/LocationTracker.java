package congybk.com.mapmvp.models.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import congybk.com.mapmvp.presenters.mapview.contact.MapViewPreseterContact;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by YNC on 9/30/2016.
 */
@EBean
public class LocationTracker implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    @RootContext
    Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private MapViewPreseterContact mMapViewPresenterContact;

    public void init(MapViewPreseterContact mapViewPreseterContact) {
        mMapViewPresenterContact = mapViewPreseterContact;
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMapViewPresenterContact.loadLocationError();
            return;

        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            mMapViewPresenterContact.loadLocationSucces(location);

        } else {
            mMapViewPresenterContact.loadLocationError();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mMapViewPresenterContact.loadLocationError();
    }

    public void connectLocation() {
        mGoogleApiClient.connect();
    }

    public void disconnectLocation() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

}
