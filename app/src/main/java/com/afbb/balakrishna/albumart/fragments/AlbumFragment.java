package com.afbb.balakrishna.albumart.fragments;

import android.app.LoaderManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.afbb.balakrishna.albumart.Adapters.SongsCursorAdapter;
import com.afbb.balakrishna.albumart.DargDropActivity;
import com.afbb.balakrishna.albumart.MainActivity;
import com.afbb.balakrishna.albumart.R;
import com.afbb.balakrishna.albumart.SampleActivty;

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
        Button buttonCustomProvider = (Button) view.findViewById(R.id.btn_custom_provider);
        songsCursorAdapter = new SongsCursorAdapter(getActivity(), null);
        getActivity().getLoaderManager().initLoader(0, null, this);
        mListView.setAdapter(songsCursorAdapter);
        buttonCustomProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new CustomContentProviderDemoFragment());
                displaySampleNotification();
            }
        });
        return view;
    }

    public void displaySampleNotification()
    {
        Log.i("Start", "notification");

   /* Invoking the default notification service */
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(getActivity());

        mBuilder.setContentTitle("New Message");
        mBuilder.setContentText("You've received new message.");
        mBuilder.setTicker("New Message Alert!");
        mBuilder.setSmallIcon(R.drawable.profile);

   /* Increase notification number every time a new notification arrives */
        mBuilder.setNumber(++numMessages);

   /* Add Big View Specific Configuration */
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        String[] events = new String[6];
        events[0] = new String("This is first line....");
        events[1] = new String("This is second line...");
        events[2] = new String("This is third line...");
        events[3] = new String("This is 4th line...");
        events[4] = new String("This is 5th line...");
        events[5] = new String("This is 6th line...");

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Big Title Details:");

        // Moves events into the big view
        for (int i=0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        mBuilder.setStyle(inboxStyle);

   /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(getActivity(), DargDropActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        stackBuilder.addParentStack(SampleActivty.class);

   /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

   /* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(notificationID++, mBuilder.build());
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
