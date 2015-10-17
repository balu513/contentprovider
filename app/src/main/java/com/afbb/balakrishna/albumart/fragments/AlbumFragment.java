package com.afbb.balakrishna.albumart.fragments;

import android.app.LoaderManager;
import android.app.NotificationManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.afbb.balakrishna.albumart.Adapters.SongsCursorAdapter;
import com.afbb.balakrishna.albumart.R;

import java.util.HashMap;
import java.util.List;

public class AlbumFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private List<HashMap> mMusicList;
    private SongsCursorAdapter songsCursorAdapter;
    private int numMessages;
    private NotificationManager mNotificationManager;
    private int notificationID;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_album, null);
        ListView mListView = (ListView) view.findViewById(R.id.list_album);
        songsCursorAdapter = new SongsCursorAdapter(getActivity(), null);

        getActivity().getLoaderManager().initLoader(0, null, this);
        mListView.setAdapter(songsCursorAdapter);


        return view;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] columnsToRetrieve = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION
        };
        String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
        return new CursorLoader(getActivity(), uri, columnsToRetrieve, where, null, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        songsCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader loader) {
        songsCursorAdapter.swapCursor(null);

    }
}
