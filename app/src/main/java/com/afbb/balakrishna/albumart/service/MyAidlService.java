package com.afbb.balakrishna.albumart.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.afbb.balakrishna.albumart.IMyAidlInterface;

import java.util.Random;


public class MyAidlService extends Service {

    String name;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    IMyAidlInterface.Stub iMyAidlInterface = new IMyAidlInterface.Stub() {

        @Override
        public void setName(String name) throws RemoteException {
            MyAidlService.this.name = name;
        }

        @Override
        public String getName() throws RemoteException {
            return MyAidlService.this.name + " : " + new Random().nextInt(1000);
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };

}
