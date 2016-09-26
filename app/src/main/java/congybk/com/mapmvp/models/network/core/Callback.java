package congybk.com.mapmvp.models.network.core;

import android.util.Log;

import retrofit.RetrofitError;
import retrofit.client.Response;

import congybk.com.mapmvp.models.network.Error;
/**
 * Copyright © 2016 AsianTech inc.
 * Created by YNC on 23/09/2016.
 */
public abstract class Callback<T> implements retrofit.Callback<T> {

    private static final String TAG = Callback.class.getSimpleName();

    public Callback() {
    }

    public abstract void success(T t);

    public abstract void failure(RetrofitError error, Error myError);

    @Override
    public void success(T t, Response response) {
        success(t);
    }

    public void failure(RetrofitError error) {
        if (error.getResponse() != null) {
            try {
                Error myError = (Error) error.getBodyAs(Error.class);
                failure(error, myError);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                Error serverError = new Error(503, "Service Unavailable");
                failure(error, serverError);
            }
        } else {
            failure(error, new Error(0, null));
        }
    }

}
