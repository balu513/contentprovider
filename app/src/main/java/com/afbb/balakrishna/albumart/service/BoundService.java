package com.afbb.balakrishna.albumart.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.afbb.balakrishna.albumart.activities.ServiceOperationsActivity;

import java.util.Calendar;

public class BoundService extends Service {
    ServiceOperationsActivity activity;
    private TextView tvServiceStatus;

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


    Handler handler = new Handler();
    private Calendar calendar;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            calendar = Calendar.getInstance();
            String currentTime = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
            Log.d("BoundService", "run 69 " + currentTime);
            tvServiceStatus.setText(currentTime);
            handler.postDelayed(runnable, 1000);
        }
    };

    public void startHandlerProcess(TextView tvServiceStatus) {
        this.tvServiceStatus = tvServiceStatus;
        Toast.makeText(getApplicationContext(), "start playing", Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(runnable);

            }
        }).start();
    }

    public void stopHandlerProcess() {
        Toast.makeText(getApplicationContext(), "stop playing", Toast.LENGTH_SHORT).show();
        handler.removeCallbacks(runnable);
    }
}
