package com.afbb.balakrishna.albumart.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.afbb.balakrishna.albumart.R;

public class MessagesAdapter extends CursorAdapter {

    public MessagesAdapter(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.add_item_messages, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tv_msg_body = (TextView) view.findViewById(R.id.textView_msgbody);
        TextView tv_msgSender = (TextView) view.findViewById(R.id.textView_msgsender);

        tv_msg_body.setText(cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY)));
        tv_msgSender.setText(cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE_SENT)));
        Log.d("MessagesAdapter", "bindView 33 " +cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS)));

    }
}
