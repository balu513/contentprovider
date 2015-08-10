package com.afbb.balakrishna.albumart.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.afbb.balakrishna.albumart.MainActivity;
import com.afbb.balakrishna.albumart.R;

import java.util.Random;

public class MyWidgetProvider extends AppWidgetProvider {

    private int[] id;

    @Override
    public void onReceive(Context context, Intent intent) {
        // this method is called before call any overridden  method of this class.
        if (intent.getAction().equals("update")) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context,
                    MyWidgetProvider.class);
            id = manager.getAppWidgetIds(thisWidget);
            onUpdate(context, manager, id);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // called periodically.
        int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent, 0);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widgetlayout);
            remoteViews.setTextViewText(R.id.textView_widget, new Random().nextInt(1000) + "");
            remoteViews.setOnClickPendingIntent(R.id.textView_widget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
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

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        // this is called when we delete any widget from the home screen (here appWidgetIds is id of deleted widget.).
    }
}
