package com.afbb.balakrishna.albumart.Utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by balakrishna on 24/7/15.
 */
public class Utils {

    public static Utils instance;
    Context context;

    public Utils(Context context) {
        this.context = context;
    }

    public static Utils getInstance(Context context) {
        if (instance == null)
            instance = new Utils(context);
        return instance;
    }

    public String getPath(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static Bitmap decodeSampledBitmapFromResource(String resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
// Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 2;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public static interface iConst {
        public static final String ID = "id";
        public static final String DATA = "data";
        public static final String TITLE = "title";
        public static final String ALBUM_IMAGE = "image";

        public static final String ARTIST = "artist";
        public static final String ALBUM = "album";
        public static final String TRACK = "track";
        public static final String DURATION = "duration";


    }


}
