package congybk.com.mapmvp;

import android.app.Application;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EApplication;

import congybk.com.mapmvp.models.network.core.ApiClient;
import congybk.com.mapmvp.models.network.core.ApiConfig;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by YNC on 9/24/2016.
 */
@EApplication
public class MyApplication extends Application{
    @AfterInject
    void init(){
        RealmConfiguration configRealm = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(configRealm);
        //Setup Api client
        ApiConfig apiConfig = ApiConfig.builder(getApplicationContext())
                .setBaseUrl(getApplicationContext().getResources().getString(R.string.url_base))
                .build();
        ApiClient.getInstance().init(apiConfig);
    }
}
