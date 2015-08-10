package com.afbb.balakrishna.albumart;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.afbb.balakrishna.albumart.activities.ActionBarDemoActiviy;
import com.afbb.balakrishna.albumart.fragments.AlbumFragment;
import com.afbb.balakrishna.albumart.fragments.ContactsFragment;
import com.afbb.balakrishna.albumart.fragments.GalleryFragment;
import com.afbb.balakrishna.albumart.fragments.VideoFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class MainActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SlidingMenu menu;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = (NavigationView) findViewById(R.id.navgview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlout);
        navigationView.setNavigationItemSelectedListener(this);
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

        }

        return false;
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
