package com.afbb.balakrishna.albumart.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afbb.balakrishna.albumart.R;
import com.afbb.balakrishna.albumart.Utils.Utils;

import java.util.HashMap;
import java.util.List;

public class VideoAdapter extends BaseAdapter {
    private final Context context;
    private final List<HashMap> list;

    public VideoAdapter(Context context, List<HashMap> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.add_songs_videos_item, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_song_video_info);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_song_video_album);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv.setText(list.get(position).get(Utils.iConst.TITLE).toString());
        holder.iv.setImageBitmap((Bitmap) list.get(position).get(Utils.iConst.ALBUM_IMAGE));
        return convertView;
    }

    private class Holder {
        TextView tv;
        ImageView iv;
    }
}
