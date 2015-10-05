package com.afbb.balakrishna.albumart.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afbb.balakrishna.albumart.R;

import java.util.ArrayList;
import java.util.List;

public class ListviewPagenationActivity extends Activity {

    private int currentFirstVisibleItem;
    private int currentVisibleItemCount;
    private int currentScrollState;
    private ListView listView;
    int count;
    private ArrayList list;
    private PaginationAdapter paginationAdapter;
    private boolean loadingMore;
    int size;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList();
        setContentView(R.layout.listview_pagination);
        listView = (ListView) findViewById(R.id.lv_pagination);
        size = 20;
        list = (ArrayList) getList(size);
        paginationAdapter = new PaginationAdapter(this, list);
        listView.setAdapter(paginationAdapter);
    }

    public List getList(int n) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < n; i++) {
            arrayList.add(i);
        }
        loadingMore = false;
        return arrayList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount)) {
                    Toast.makeText(getApplicationContext(), "load more :)", Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    size = size + 20;
                    list.clear();
                    list.addAll(getList(size));
                    paginationAdapter.notifyDataSetChanged();
                    listView.setSelection(firstVisibleItem);
                }
            }
        });
    }


    class PaginationAdapter extends BaseAdapter {

        private final Context context;
        private final List list;

        public PaginationAdapter(Context context, List list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return list.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.additem_lv_pagination, null);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_additem_pagination);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv.setText(list.get(position) + "");
            return convertView;
        }

        class ViewHolder {
            TextView tv;
        }
    }
}
