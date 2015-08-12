package com.afbb.balakrishna.albumart.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import java.util.Random;

public class MyMessengerService extends Service {

    Messenger messenger;

    public MyMessengerService() {
        messenger = new Messenger(handler);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    Bundle data = msg.getData();
                    String message = data.getString("key_fromActivity");
                    data.putString("key_fromService", "from service  " + new Random().nextInt(10000));
                    Toast.makeText(getApplicationContext(), "message:"+message, Toast.LENGTH_SHORT).show();

                    Message message1 = new Message();
                    message1.setData(data);
                    message1.what = 101;
                    try {
                        msg.replyTo.send(message1);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
