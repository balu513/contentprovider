package com.afbb.balakrishna.albumart.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

import com.afbb.balakrishna.albumart.R;
import com.afbb.balakrishna.albumart.service.MyMessengerService;

import java.util.Random;

public class MessengerActivity extends Activity {

    private TextView tv_msg_fromAct;
    private TextView tv_msg_fromService;
    private Messenger mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messanger);
        tv_msg_fromAct = (TextView) findViewById(R.id.tv_msg_from_activity);
        tv_msg_fromService = (TextView) findViewById(R.id.tv_msg_from_service);
        Intent intent = new Intent(this, MyMessengerService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
//        createPendingResult()
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    Messenger messenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 101 is when msg send from messengerService
                case 101:
                    Bundle data = msg.getData();
                    String messageFromService = data.getString("key_fromService");
                    String messageActivity = data.getString("key_fromActivity");
                    tv_msg_fromService.setText(tv_msg_fromService.getText() + "\n\n" + messageFromService);
                    tv_msg_fromAct.setText(tv_msg_fromAct.getText() + "\n\n" +messageActivity);

            }
        }
    });

    public void send(View view) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("key_fromActivity", "from Activity  " + new Random().nextInt(10000));
        message.setData(bundle);
        message.what = 100;
        try {
            mService.send(message);
            message.replyTo = messenger;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

}
