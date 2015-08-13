package com.afbb.balakrishna.albumart.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class SingletonService extends Service {

    private static SingletonService instance;

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;

    }

    public static SingletonService getInstance() {
        return instance;
    }

    public void getDataFromActivty(String data) {
        Toast.makeText(getApplicationContext(), "data from activity: " + data, Toast.LENGTH_SHORT).show();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
