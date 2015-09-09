package com.afbb.balakrishna.albumart.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.afbb.balakrishna.albumart.IMyAidlInterface;
import com.afbb.balakrishna.albumart.R;
import com.afbb.balakrishna.albumart.service.MyAidlService;

public class AidlActivity extends Activity {

    IMyAidlInterface iMyAidlInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        Intent intent = new Intent(this, MyAidlService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
        startService(intent);
    }

    private MyAidlService myAidlService;
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            try {
                iMyAidlInterface.setName("BALUUU");

                Toast.makeText(getApplicationContext(), "NAME: " + iMyAidlInterface.getName(), Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myAidlService = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null)
            unbindService(conn);
    }
}
