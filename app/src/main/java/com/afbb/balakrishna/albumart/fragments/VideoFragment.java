package com.afbb.balakrishna.albumart.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.afbb.balakrishna.albumart.Adapters.VideoAdapter;
import com.afbb.balakrishna.albumart.MainActivity;
import com.afbb.balakrishna.albumart.R;
import com.afbb.balakrishna.albumart.Utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class VideoFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, null);
        ListView listview = (ListView) view.findViewById(R.id.list_videos);
        VideoAdapter adapter = new VideoAdapter(((MainActivity) (getActivity())), getVideos());
        listview.setAdapter(adapter);

        return view;
    }

    public ArrayList<HashMap> getVideos() {
        ArrayList<HashMap> list = new ArrayList();
        HashMap hashMap = null;
        Uri contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        cursor.moveToFirst();
        String[] columnNames = cursor.getColumnNames();
        String s = Arrays.toString(columnNames);
        //[_id, _data, _display_name, _size, mime_type, date_added, date_modified, title, duration, artist, album, resolution, description, isprivate, tags, category, language,
        // mini_thumb_data, latitude, longitude, datetaken, mini_thumb_magic, bucket_id, bucket_display_name, bookmark, width, height]
        Log.d("VideoFragment", "getVideos 30 " + s);
        do {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
            String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
            Bitmap thumbnail = getVideoThumbnail(id);
            hashMap = new HashMap();
            hashMap.put(Utils.iConst.ID, id);
            hashMap.put(Utils.iConst.DATA, data);
            hashMap.put(Utils.iConst.TITLE, title);
            hashMap.put(Utils.iConst.ALBUM_IMAGE, thumbnail);

            list.add(hashMap);

        } while (cursor.moveToNext());

        return list;

    }

    public Bitmap getVideoThumbnail(int id) {
        ContentResolver crThumb = getActivity().getContentResolver();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);
        return curThumb;
    }
}
