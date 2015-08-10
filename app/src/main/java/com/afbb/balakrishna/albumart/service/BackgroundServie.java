package com.afbb.balakrishna.albumart.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BackgroundServie extends Service {

	ScheduledExecutorService service;

	@Override
    public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		service = Executors.newSingleThreadScheduledExecutor();
	}

	Thread thread = new Thread(new Runnable() {

		@Override
		public void run() {

			Calendar calender = Calendar.getInstance();
			int Hour = calender.getTime().getHours();
			int minute = calender.getTime().getMinutes();
			String time = "" + Hour + " : " + minute;
			System.out.println("Service:     "+time);
			Intent intent=new Intent();
			intent.putExtra("key", time);
			intent.setAction("UPDATE");
			sendBroadcast(intent);
		}
	});

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		service.scheduleAtFixedRate(thread, 0, 10, TimeUnit.SECONDS);
		return super.onStartCommand(intent, flags, startId);
	}

}