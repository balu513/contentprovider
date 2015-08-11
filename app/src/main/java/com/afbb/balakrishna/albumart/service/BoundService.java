package com.afbb.balakrishna.albumart.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.afbb.balakrishna.albumart.activities.ServiceOperationsActivity;

import java.util.Calendar;

public class BoundService extends Service {
    ServiceOperationsActivity activity;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BoundService", "onCreate 16 ");
        Toast.makeText(getApplicationContext(), "onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("BoundService", "onBind 23 ");
        Toast.makeText(getApplicationContext(), "onBind", Toast.LENGTH_SHORT).show();
        return new MyLocale();
    }

    Handler handler = new Handler();
    private Calendar calendar;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (calendar == null)
                calendar = Calendar.getInstance();

            sendBackCurrentProcessStatus(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));

        }
    };

    public class MyLocale extends Binder {
        public BoundService getReff() {
            return BoundService.this;
        }

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("BoundService", "onUnbind 37 ");
        Toast.makeText(getApplicationContext(), "onUnBind", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d("BoundService", "onDestroy 44 ");
        Toast.makeText(getApplicationContext(), "onDestroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }


    public void setActivityReference(ServiceOperationsActivity activity) {
        if (this.activity == null)
            this.activity = activity;
    }

    public void sendBackCurrentProcessStatus(String currentTime) {
        Log.d("BoundService", "sendBackCurrentProcessStatus 73 " );
        activity.sendBackCurrentProcessStatus(currentTime);
        handler.postDelayed(runnable, 400);
    }

    public void startHandlerProcess() {
        Toast.makeText(getApplicationContext(), "start playing", Toast.LENGTH_SHORT).show();
        handler.postDelayed(runnable, 1000);
    }

    public void stopHandlerProcess() {
        Toast.makeText(getApplicationContext(), "stop playing", Toast.LENGTH_SHORT).show();
        handler.removeCallbacks(runnable);
    }
}
