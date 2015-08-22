package com.afbb.balakrishna.albumart.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.System;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.afbb.balakrishna.albumart.R;

public class TourchActivity extends Activity implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private Handler handler;
    private ImageView imageView;
    private ToggleButton toggleFlash;
    private int i;
    private boolean isON;

    private Camera cam;
    private SeekBar seekBar;
    private Handler handlerFlashOnOff;
    private int TimeGap;

    private int color;
    private TextView viewColors;
    private Handler handler2;
    private SeekBar brightbar;
    private ContentResolver cResolver;
    private Window window;
    private int brightness;


    Runnable runnableBatteryLevel = new Runnable() {
        @Override
        public void run() {
            if (i > 3) {
                i = 0;
            }
            imageView.setImageLevel(i++);
            handler.postDelayed(runnableBatteryLevel, 1000);

        }
    };
    private TextView tvBrightness;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourchlight);
        toggleFlash = (ToggleButton) findViewById(R.id.toggleButton_flashonoff);
        seekBar = (SeekBar) findViewById(R.id.seekbar_flash);
        brightbar = (SeekBar) findViewById(R.id.seekbar_brightness);
        imageView = (ImageView) findViewById(R.id.imageView_battery);
        Button btnRed = (Button) findViewById(R.id.buttonRED);
        Button btnBlue = (Button) findViewById(R.id.buttonBLUE);
        Button btnGray = (Button) findViewById(R.id.buttonGRAY);
        Button btnGreen = (Button) findViewById(R.id.buttonGREEN);
        Button btnPink = (Button) findViewById(R.id.buttonPINK);
        Button btnVoilet = (Button) findViewById(R.id.buttonVOILET);
        Button btnYellow = (Button) findViewById(R.id.buttonYELLOW);
        tvBrightness = (TextView) findViewById(R.id.tv_brigtness);
        viewColors = (TextView) findViewById(R.id.view_color_change);
        btnRed.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnGray.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        btnPink.setOnClickListener(this);
        btnVoilet.setOnClickListener(this);
        btnYellow.setOnClickListener(this);


        handler = new Handler();
        handlerFlashOnOff = new Handler();
        handler2 = new Handler();
        handler.post(runnableBatteryLevel);
        toggleFlash.setOnCheckedChangeListener(this);
        seekBar.setOnSeekBarChangeListener(this);

        boolean isFlashAvailable = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!isFlashAvailable) {
            Toast.makeText(getApplicationContext(), "flash not available", Toast.LENGTH_SHORT).show();
            return;
        }


        cResolver = getContentResolver();

        //Get the current window
        window = getWindow();

        //Set the seekbar range between 0 and 255
        brightbar.setMax(255);
        //Set the seek bar progress to 1
        brightbar.setKeyProgressIncrement(1);

        try {
            //Get the current system brightness
            brightness = System.getInt(cResolver, System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            //Throw an error case it couldn't be retrieved
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }

        //Set the progress of the seek bar based on the system's brightness
        brightbar.setProgress(brightness);

    }

    @Override
    protected void onResume() {
        super.onResume();

        brightbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Set the system brightness using the brightness variable value
                System.putInt(cResolver, System.SCREEN_BRIGHTNESS, brightness);
                //Get the current window attributes
                WindowManager.LayoutParams layoutpars = window.getAttributes();
                //Set the brightness of this window
                layoutpars.screenBrightness = brightness / (float) 255;
                //Apply attribute changes to this window
                window.setAttributes(layoutpars);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                //Nothing handled here
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Set the minimal brightness level
                //if seek bar is 20 or any value below
                if (progress <= 20) {
                    //Set the brightness to 20
                    brightness = 20;
                } else //brightness is greater than 20
                {
                    //Set brightness variable based on the progress bar
                    brightness = progress;
                }
                //Calculate the brightness percentage
                float perc = (brightness / (float) 255) * 100;
                //Set the brightness percentage
                tvBrightness.setText((int) perc + " %");
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            turnOnFlashLight();
        } else {
            if (handlerFlashOnOff != null)
                handlerFlashOnOff.removeCallbacks(runnable1);
            turnOffFlashLight();
        }

    }

    public void turnOnFlashLight() {
        isON = true;
        try {
            cam = Camera.open();
            Camera.Parameters p = cam.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
            cam.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Exception throws in turning on flashlight.", Toast.LENGTH_SHORT).show();
        }
    }

    public void turnOffFlashLight() {
        isON = false;
        try {
            cam.stopPreview();
            cam.release();
            cam = null;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Exception throws in turning off flashlight.", Toast.LENGTH_SHORT).show();
        }
    }

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            oNOff(isON);
            handler.postDelayed(runnable1, TimeGap * 100);

        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
        TimeGap = progress;
        handlerFlashOnOff.removeCallbacks(runnable1);
        handlerFlashOnOff.post(runnable1);
    }

    private void oNOff(boolean isON) {
        if (isON)
            turnOffFlashLight();
        else
            turnOnFlashLight();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handlerFlashOnOff.post(runnable1);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    boolean isBlack;
    int colorOnOffCount = 0;
    Runnable runnableColors = new Runnable() {
        @Override
        public void run() {
            if (isBlack) {
                isBlack = false;
                viewColors.setBackgroundColor(color);
            } else {
                isBlack = true;
                viewColors.setBackgroundColor(getResources().getColor(R.color.black));
            }
            Log.d("TourchActivity", "run 248 " + colorOnOffCount);

            colorOnOffCount++;
            handler2.postDelayed(runnableColors, 300);

            if (colorOnOffCount == 10) {
                handler2.removeCallbacks(runnableColors);
                //handler2 = null;
                colorOnOffCount = 0;
            }
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.buttonBLUE:
                color = getResources().getColor(R.color.blue);
                break;
            case R.id.buttonGRAY:
                color = getResources().getColor(R.color.gray);
                break;
            case R.id.buttonGREEN:
                color = getResources().getColor(R.color.green);
                break;
            case R.id.buttonPINK:
                color = getResources().getColor(R.color.pink);
                break;
            case R.id.buttonRED:
                color = getResources().getColor(R.color.red);
                break;
            case R.id.buttonVOILET:
                color = getResources().getColor(R.color.volilet);
                break;
            case R.id.buttonYELLOW:
                color = getResources().getColor(R.color.yellow);
                break;

        }
        handler2.removeCallbacks(runnableColors);
        handler2.post(runnableColors);
        viewColors.setBackgroundColor(color);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cam != null)
            cam.release();
        cam = null;
    }
}