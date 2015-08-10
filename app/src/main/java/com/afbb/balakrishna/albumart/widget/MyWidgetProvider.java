package com.afbb.balakrishna.albumart.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.afbb.balakrishna.albumart.R;
import com.afbb.balakrishna.albumart.activities.WidgetActivty;

import java.util.Random;

public class MyWidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            Intent intent = new Intent(context, WidgetActivty.class);
            PendingIntent.getActivity(context, 100, intent, 0);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widgetlayout);
            remoteViews.setTextViewText(R.id.textView_widget, new Random().nextInt(1000) + "");
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        //it is called only once even if many same widgets created on home screen
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        //this is called once when we remove all the widgets from home screen, ie at the time of removing last widget from the screen this method is called.
    }
}
