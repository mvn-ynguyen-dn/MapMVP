package congybk.com.mapmvp.models.network;


import java.util.List;

import congybk.com.mapmvp.models.objects.ResultMarker;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by YNC on 23/09/2016.
 */
public interface Api {
    @GET("/bins/594w8")
    void getListMarker(Callback<List<ResultMarker>> marker);
}
