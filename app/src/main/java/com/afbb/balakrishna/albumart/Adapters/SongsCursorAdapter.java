package com.afbb.balakrishna.albumart.Adapters;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afbb.balakrishna.albumart.R;

public class SongsCursorAdapter extends CursorAdapter {

    private final Context context;
    private final Cursor c;


    public SongsCursorAdapter(Context context, Cursor c) {
        super(context, c, true);
        this.context = context;
        this.c = c;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.add_songs_videos_item, null);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView tv = (TextView) view.findViewById(R.id.tv_song_video_info);
        final ImageView iv = (ImageView) view.findViewById(R.id.iv_song_video_album);


        long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
        String track = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));

        String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
        String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
        String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

        final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
        final Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, albumId);

        tv.setText(track);
        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), albumArtUri);
                } catch (Exception exception) {
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                iv.setImageBitmap(bitmap);
            }
        }.execute();
    }

}
