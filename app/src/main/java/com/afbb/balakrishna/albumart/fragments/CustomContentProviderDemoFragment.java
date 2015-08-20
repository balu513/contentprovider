package com.afbb.balakrishna.albumart.fragments;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afbb.balakrishna.albumart.R;
import com.afbb.balakrishna.albumart.provider.Const_Provider;
import com.afbb.balakrishna.albumart.provider.MyProvider;

public class CustomContentProviderDemoFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ListView listview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_custom_content_provider_demo, null);
        listview = (ListView) view.findViewById(R.id.listview_customprovider);
        init();
        return view;
    }

    private void init() {
        Button btn_show = (Button) view.findViewById(R.id.btn_show);
        Button btn_insert = (Button) view.findViewById(R.id.btn_insert);
        Button btn_update = (Button) view.findViewById(R.id.btn_update);
        Button btn_del = (Button) view.findViewById(R.id.btn_delete);
        btn_show.setOnClickListener(this);
        btn_insert.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        showData();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_show:
                showData();
                break;
            case R.id.btn_insert:
                insertRecord();
                break;
            case R.id.btn_update:
                updateRecord();
                break;
            case R.id.btn_delete:
                deleteRecord();
                break;

        }

    }

    private void deleteRecord() {

    }

    private void updateRecord() {

    }

    private void insertRecord() {
        ContentValues values = new ContentValues();
        values.put(Const_Provider., "BALU");
        Uri uri = getActivity().getContentResolver().insert(MyProvider.CONTENT_URI, values);
        Toast.makeText(getActivity(), "New record inserted", Toast.LENGTH_LONG)
                .show();
    }

    private void showData() {
        Cursor cursor = getActivity().getContentResolver().query(MyProvider.CONTENT_URI, null, null, null, null);
        MyAdapter adapter = new MyAdapter(getActivity(), cursor);
        listview.setAdapter(adapter);


    }

    private class MyAdapter extends CursorAdapter {

        private final Context context;
        private final Cursor c;

        public MyAdapter(Context context, Cursor c) {
            super(context, c, true);
            this.context = context;
            this.c = c;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.add_item_customprovider, null);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tv = (TextView) view.findViewById(R.id.textView_additem_custom_provider);
            String name = cursor.getString(cursor.getColumnIndex(MyProvider.name));
            tv.setText(name);
        }
    }
}