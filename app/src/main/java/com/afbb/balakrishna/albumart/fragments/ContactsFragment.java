package com.afbb.balakrishna.albumart.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afbb.balakrishna.albumart.Adapters.ContactsCursorAdapter;
import com.afbb.balakrishna.albumart.MainActivity;
import com.afbb.balakrishna.albumart.R;

import java.util.Arrays;
import java.util.List;

public class ContactsFragment extends Fragment {

    private ListView listview;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Cursor cursor = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        View view = inflater.inflate(R.layout.fragment_contacts, null);
        listview = (ListView) view.findViewById(R.id.list_contacts);
        final ContactsCursorAdapter adapter = new ContactsCursorAdapter(getActivity(), cursor);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                String contactId = cursor.getString(cursor.getColumnIndex(""));

                Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                String[] columnNames = phones.getColumnNames();
                List<String> list = Arrays.asList(columnNames);

                phones.moveToNext();
                String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                ((MainActivity) getActivity()).replaceFragment(new MessagesFragment(), number);
            }
        });

        return view;
    }
}
