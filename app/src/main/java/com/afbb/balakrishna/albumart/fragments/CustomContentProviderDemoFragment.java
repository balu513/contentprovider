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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afbb.balakrishna.albumart.R;
import com.afbb.balakrishna.albumart.provider.Const_Provider;

public class CustomContentProviderDemoFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ListView listview;
    private EditText et_name;

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
        et_name = (EditText) view.findViewById(R.id.et_name);
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
                showData();
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
        values.put(Const_Provider.COL_STUDENT_NAME, et_name.getText() + "");
        values.put(Const_Provider.COL_STUDENT_BRANCH, "CSE");
        Uri uri = getActivity().getContentResolver().insert(Const_Provider.CONTENT_URI_STUDENTS, values);
        Toast.makeText(getActivity(), "New record " + et_name.getText() + "inserted", Toast.LENGTH_LONG)
                .show();
    }

    private void showData() {
        Cursor cursor = getActivity().getContentResolver().query(Const_Provider.CONTENT_URI_STUDENTS, null, null, null, null);
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
            String name = cursor.getString(cursor.getColumnIndex(Const_Provider.COL_STUDENT_NAME));
            tv.setText(name);
        }
    }
}