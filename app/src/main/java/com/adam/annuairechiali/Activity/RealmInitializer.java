package com.adam.annuairechiali.Activity;

import android.app.Application;

import com.adam.annuairechiali.Manager.MyMigration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmInitializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // The default Realm file is "default.realm" in Context.getFilesDir();
        // we'll change it to "myrealm.realm"
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm").
                  schemaVersion(2) // Must be bumped when the schema changes
                .migration(new MyMigration())
        .build();
        Realm.setDefaultConfiguration(config);
    }
}