package com.afbb.balakrishna.albumart.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.afbb.balakrishna.albumart.Adapters.GalleryAdapter;
import com.afbb.balakrishna.albumart.R;

public class GalleryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_gallery, null);
        GridView gridview = (GridView) view.findViewById(R.id.gridView_glaimages);
        Cursor cursor = getImages();
        GalleryAdapter galleryAdapter = new GalleryAdapter(getActivity(), cursor);
        gridview.setAdapter(galleryAdapter);
        return view;
    }

    public Cursor getImages() {
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        //[_id, thread_id, address, person, date, date_sent, protocol, read, status, type, reply_path_present, subject, body, service_center, failure_cause, locked, sub_id, stack_type, error_code, creator, seen]



        return cursor;
    }
}
