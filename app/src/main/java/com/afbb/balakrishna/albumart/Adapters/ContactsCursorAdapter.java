package com.afbb.balakrishna.albumart.Adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afbb.balakrishna.albumart.R;

import java.io.IOException;

public class ContactsCursorAdapter extends CursorAdapter {

    private final Context context;
    private final Cursor c;
    private Cursor phones;


    public ContactsCursorAdapter(Context context, Cursor c) {
        super(context, c, true);
        this.context = context;
        this.c = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.add_contact_item, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tv_displayName = (TextView) view.findViewById(R.id.tv_contact_name);
        TextView tv_phoneNo = (TextView) view.findViewById(R.id.tv_phone_number);
        ImageView iv_contact = (ImageView) view.findViewById(R.id.iv_contact_image);

        String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        tv_displayName.setText(displayName);

        try {
            String uriString = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
            if (uriString != null) {
                Uri imageUri = Uri.parse(uriString);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                iv_contact.setImageBitmap(bitmap);
            }
            ContentResolver cr = context.getContentResolver();
            Cursor cursor3 = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                    "DISPLAY_NAME = '" + displayName + "'", null, null);

            if (cursor3.moveToFirst()) {
                String contactId = cursor3.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                phones.moveToNext();
                String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                tv_phoneNo.setText(number);
                phones.close();

            }
            cursor3.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
