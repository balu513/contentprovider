package com.afbb.balakrishna.albumart;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.afbb.balakrishna.albumart.activities.ActionBarDemoActiviy;
import com.afbb.balakrishna.albumart.activities.ServiceOperationsActivity;
import com.afbb.balakrishna.albumart.fragments.AlbumFragment;
import com.afbb.balakrishna.albumart.fragments.ContactsFragment;
import com.afbb.balakrishna.albumart.fragments.GalleryFragment;
import com.afbb.balakrishna.albumart.fragments.VideoFragment;
import com.afbb.balakrishna.albumart.service.BackgroundServie;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.Calendar;


public class MainActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SlidingMenu menu;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private int i;
    private Intent intentStartService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = (NavigationView) findViewById(R.id.navgview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlout);
        navigationView.setNavigationItemSelectedListener(this);
        startService(new Intent(this, BackgroundServie.class));
        replaceFragment(new AlbumFragment());
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        closeDrawer();
        switch (menuItem.getItemId()) {
            case R.id.menu_audio:
                replaceFragment(new AlbumFragment());
                break;
            case R.id.menu_video:
                replaceFragment(new VideoFragment());
                break;
            case R.id.menu_gallery:
                replaceFragment(new GalleryFragment());
                break;
            case R.id.menu_contacts:
                replaceFragment(new ContactsFragment());
                break;
            case R.id.menu_tabs:
                startActivity(new Intent(this, ActionBarDemoActiviy.class));
                break;
            case R.id.menu_service:
                startActivity(new Intent(this, ServiceOperationsActivity.class));
                break;
            case R.id.menu_messengers:
                break;
            case R.id.menu_aidl:
                break;


        }

        return false;
    }

    private void showNotification() {
        Toast.makeText(getApplicationContext(), "show notification", Toast.LENGTH_SHORT).show();
        Calendar calendar = Calendar.getInstance();
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("New mail from balu")
                .setContentText("yehh : " + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND))
                .setSmallIcon(R.drawable.profile)
//                .setOngoing(true)
//                .setLargeIcon(aBitmap)
                .build();
        i++;
        notification.contentIntent = pendingIntent;
        mNotifyMgr.notify(i, notification);
//        mNotifyMgr.cancel(i);

    }


    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void replaceFragment(Fragment fragment, String data) {
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void closeDrawer() {
        drawerLayout.closeDrawers();
    }


}
