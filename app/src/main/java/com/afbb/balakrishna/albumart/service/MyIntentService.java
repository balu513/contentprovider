package com.afbb.balakrishna.albumart.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

public class MyIntentService extends IntentService {



    public MyIntentService() {
        super("balu");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle bundle = intent.getExtras();
        ResultReceiver receiver = bundle.getParcelable("key_myResultReceiver");
        bundle.putString("from_intent_service", "Im from intent service");
        receiver.send(200, bundle);

    }
}
