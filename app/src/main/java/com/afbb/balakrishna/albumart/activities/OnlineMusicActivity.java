package com.afbb.balakrishna.albumart.activities;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afbb.balakrishna.albumart.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class OnlineMusicActivity extends Activity {

    private TextView tv_channel_name;
    private TextView tv_channel_url;
    private SeekBar seekbar_downloadProgress;
    public static final String song_url = "http://programmerguru.com/android-tutorial/wp-content/uploads/2013/04/hosannatelugu.mp3";
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_music);
        tv_channel_name = (TextView) findViewById(R.id.tv_channel_name);
        tv_channel_url = (TextView) findViewById(R.id.tv_url);
        seekbar_downloadProgress = (SeekBar) findViewById(R.id.seekBar_download_progress);

        tv_channel_url.setText(song_url);
        tv_channel_name.setText("HOSANNA");


    }

    public void startMusic(View v) {
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(song_url);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        try {
            mPlayer.prepare();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        mPlayer.start();

    }

    public void stopMusic(View v) {
        Toast.makeText(getApplicationContext(), "stop music", Toast.LENGTH_SHORT).show();
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer = null;
        }
    }


    public void downloadSong(View v) {
        Toast.makeText(getApplicationContext(), "start downloading ..", Toast.LENGTH_SHORT).show();
        new DownLoadSong(10000, 1000, song_url).start();


    }

    class DownLoadSong extends CountDownTimer {
        private final long millisInFuture;
        private final long countDownInterval;
        private final String url;
        DownLoadAsyncTask downLoadAsyncTask = null;

        public DownLoadSong(long millisInFuture, long countDownInterval, String url) {
            super(millisInFuture, countDownInterval);

            this.millisInFuture = millisInFuture;
            this.countDownInterval = countDownInterval;
            this.url = url;
            downLoadAsyncTask = new DownLoadAsyncTask();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.d("DownLoadSong", "onTick 79 " + millisUntilFinished);
            seekbar_downloadProgress.setProgress((int) ((millisInFuture - millisUntilFinished) / 100));

        }

        @Override
        public void onFinish() {
            Log.d("DownLoadSong", "onFinish 86 ");
            downLoadAsyncTask.cancel(true);

        }
    }

    class DownLoadAsyncTask extends AsyncTask<Void, Void, Void> {
        URLConnection connection = null;
        File file;
        private FileOutputStream outputStream;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            file = new File(directory.getAbsolutePath() + "/" + "song.txt");
            try {
            file.createNewFile();
                outputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                connection = new URL(song_url).openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                byte[] data = new byte[1024];
                int numRead;
                while ((numRead = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, numRead);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}

