package congybk.com.mapmvp.presenters.mapview.contact;

import android.location.Location;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by YNC on 9/30/2016.
 */
public interface MapViewPreseterContact {
    void loadLocationError();

    void loadLocationSucces(Location location);
}
