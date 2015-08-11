package com.afbb.balakrishna.albumart.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class StartService extends Service {
    Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("StartService", "onCreate 15 ");
        Toast.makeText(getApplicationContext(), "onCreate of unbind service", Toast.LENGTH_SHORT).show();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.d("BoundService", "run 20 " + "unbound service");
            handler.postDelayed(runnable, 1000);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("StartService", "onStartCommand");
        handler.postDelayed(runnable, 1000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        Log.d("StartService", "onDestroy 40 ");
        Toast.makeText(getApplicationContext(), "onDestroy of unbind service", Toast.LENGTH_SHORT).show();
    }
}
