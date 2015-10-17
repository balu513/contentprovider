package com.afbb.balakrishna.albumart.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afbb.balakrishna.albumart.R;
import com.afbb.balakrishna.albumart.service.BoundService;
import com.afbb.balakrishna.albumart.service.MyIntentService;
import com.afbb.balakrishna.albumart.service.SingletonService;
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
        init();
    }

    private void init() {
        Button btn_startService = (Button) findViewById(R.id.button_start_service);
        Button btn_stopService = (Button) findViewById(R.id.button_stop_service);
        Button btn_bindService = (Button) findViewById(R.id.button_bind_service);
        Button btn_unBindService = (Button) findViewById(R.id.button_unbind);
        Button btn_start_intent_service = (Button) findViewById(R.id.button_intentserivce_start);
        Button btn_startPlay = (Button) findViewById(R.id.button_startplay);
        Button btn_stopPlay = (Button) findViewById(R.id.button_stop_play);
        Button btn_singleton = (Button) findViewById(R.id.button_servcie_singletom);
        btn_startService.setOnClickListener(this);
        btn_stopService.setOnClickListener(this);
        btn_bindService.setOnClickListener(this);
        btn_unBindService.setOnClickListener(this);
        btn_start_intent_service.setOnClickListener(this);
        btn_startPlay.setOnClickListener(this);
        btn_stopPlay.setOnClickListener(this);
        btn_singleton.setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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
            case R.id.button_startplay:
                if (serviceReff != null)
                    serviceReff.startHandlerProcess(tvServiceStatus);
                break;
            case R.id.button_stop_play:
                if (serviceReff != null)
                    serviceReff.stopHandlerProcess();
                break;
            case R.id.button_intentserivce_start:
                startIntentService();
                break;
            case R.id.button_servcie_singletom:
                startServiceSingleTonProducure();
                break;
        }
    }

    private void startServiceSingleTonProducure() {
        Intent intent = new Intent(this, SingletonService.class);
        startService(intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SingletonService service = SingletonService.getInstance();
                service.getDataFromActivty("hi from activity ");

            }
        }, 1000);
    }

    private void startIntentService() {
        Intent intent = new Intent(this, MyIntentService.class);
        MyResultReceiver resultReceiver = new MyResultReceiver(new Handler());
        intent.putExtra("key_myResultReceiver", resultReceiver);
        intent.setAction("myIntentAction");
        startService(intent);

    }

    public class MyResultReceiver extends ResultReceiver {

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            
            String responseFromIntentservice = resultData.getString("from_intent_service");
            Toast.makeText(getApplicationContext(), "response " + responseFromIntentservice, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null && intentBoundService != null) {
            unbindService(conn);
            stopService(intentBoundService);
        }
    }
}
