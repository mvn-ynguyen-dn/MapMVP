package congybk.com.mapmvp.views;

import android.support.v4.app.Fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by YNC on 9/24/2016.
 */
@EFragment
public abstract class BaseFragment extends Fragment {
    @AfterViews
    protected abstract void init();
}
