package com.afbb.balakrishna.albumart.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.afbb.balakrishna.albumart.core.Student;

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
                    data.setClassLoader(Student.class.getClassLoader());
                    String message = data.getString("key_fromActivity");
                    Student student = data.getParcelable("key_parcel");
                    data.putString("key_fromService", "from service  " + new Random().nextInt(10000) + " student " + student.getName());

                    Message message1 = handler.obtainMessage();
                    message1.setData(data);
                    message1.what = 101;
                    try {
                        msg.replyTo.send(message1);
                    } catch (Exception e) {
                        e.printStackTrace();
//                        Log.e("")
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
