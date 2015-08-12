package com.afbb.balakrishna.albumart.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afbb.balakrishna.albumart.R;
import com.afbb.balakrishna.albumart.service.BoundService;
import com.afbb.balakrishna.albumart.service.StartService;

public class ServiceOperationsActivity extends Activity implements View.OnClickListener {

    private Intent intentStartService;
    private Intent intentBoundService;
    private TextView tvServiceStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_operatons_activty);
        tvServiceStatus = (TextView) findViewById(R.id.tv_service_proccessing);
        Button btn_startService = (Button) findViewById(R.id.button_start_service);
        Button btn_stopService = (Button) findViewById(R.id.button_stop_service);
        Button btn_bindService = (Button) findViewById(R.id.button_bind_service);
        Button btn_unBindService = (Button) findViewById(R.id.button_unbind);
        Button btn_start_intent_service = (Button) findViewById(R.id.button_intentserivce_start);
        Button btn_startPlay = (Button) findViewById(R.id.button_startplay);
        Button btn_stopPlay = (Button) findViewById(R.id.button_stop_play);
        btn_startService.setOnClickListener(this);
        btn_stopService.setOnClickListener(this);
        btn_bindService.setOnClickListener(this);
        btn_unBindService.setOnClickListener(this);
        btn_start_intent_service.setOnClickListener(this);
        btn_startPlay.setOnClickListener(this);
        btn_stopPlay.setOnClickListener(this);
    }

    private BoundService.MyLocale locale;
    private BoundService serviceReff;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            locale = (BoundService.MyLocale) service;
            serviceReff = locale.getReff();
            if (serviceReff != null) {
                serviceReff.setActivityReference(ServiceOperationsActivity.this);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceReff = null;

        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_start_service:
                intentStartService = new Intent(this, StartService.class);
                startService(intentStartService);
                break;
            case R.id.button_stop_service:
                stopService(intentStartService);
                break;
            case R.id.button_bind_service:
                intentBoundService = new Intent(this, BoundService.class);
                bindService(intentBoundService, conn, BIND_AUTO_CREATE);
                startService(intentBoundService);
                break;
            case R.id.button_unbind:
                unbindService(conn);
                stopService(intentBoundService);
                break;
            case R.id.button_intentserivce_start:
                break;
            case R.id.button_startplay:
                if (serviceReff != null)
                    serviceReff.startHandlerProcess(tvServiceStatus);
                break;
            case R.id.button_stop_play:
                if (serviceReff != null)
                    serviceReff.stopHandlerProcess();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        stopService(intentBoundService);
    }
}
