package com.bionicapps.tictactoe;

import android.app.Application;

import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by johan on 4/24/16.
 */
public class TicTacToeApplication extends Application{

    @Override public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(BuildConfig.DEBUG);

        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);

    }
}