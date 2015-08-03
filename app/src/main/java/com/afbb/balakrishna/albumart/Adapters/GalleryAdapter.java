package com.afbb.balakrishna.albumart.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afbb.balakrishna.albumart.R;

public class GalleryAdapter extends CursorAdapter {
    private final Context context;
    private final Cursor c;

    public GalleryAdapter(Context context, Cursor c) {
        super(context, c, true);
        this.context = context;
        this.c = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.add_image_gal_item, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView_gal_additem);
        TextView tv= (TextView) view.findViewById(R.id.textView_gal_img_name);
        String data = cursor.getString(cursor.getColumnIndex("_data"));
        String name = cursor.getString(cursor.getColumnIndex("_display_name"));
        Bitmap bitmap = BitmapFactory.decodeFile(data);
        imageView.setImageBitmap(bitmap);
        tv.setText(name);
        //[_id, _data, _size, _display_name, mime_type, title, date_added, date_modified, description, picasa_id, isprivate, latitude, longitude, datetaken, orientation,
        // mini_thumb_magic, bucket_id, bucket_display_name, width, height]


    }
}
