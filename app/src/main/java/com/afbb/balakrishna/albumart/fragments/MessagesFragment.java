package com.afbb.balakrishna.albumart.fragments;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.afbb.balakrishna.albumart.Adapters.MessagesAdapter;
import com.afbb.balakrishna.albumart.R;

public class MessagesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String phno = bundle.getString("data");
        View view = inflater.inflate(R.layout.fragment_contacts, null);
        ListView listView = (ListView) view.findViewById(R.id.list_contacts);
        Cursor cursor1 = getMessages(phno);
        MessagesAdapter adapter1 = new MessagesAdapter(getActivity(), cursor1);
        listView.setAdapter(adapter1);
        return view;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public Cursor getMessages(String phoneNumber) {
        Cursor cursor = getActivity().getContentResolver().query(Telephony.Sms.CONTENT_URI, null, Telephony.Sms.ADDRESS + "=" + phoneNumber, null, null);
        return cursor;
    }
}
