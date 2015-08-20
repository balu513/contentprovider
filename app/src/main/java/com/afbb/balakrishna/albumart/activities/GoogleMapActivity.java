package com.afbb.balakrishna.albumart.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.afbb.balakrishna.albumart.R;
import com.afbb.balakrishna.albumart.maps.AdressJSONParser;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class GoogleMapActivity extends ActionBarActivity {
    // Google Map
    private GoogleMap googleMap;
    private double currentLatititude;
    private double currentLongitude;
    private Bitmap bitmapMapImage;
    private MarkerOptions marker;
    private Toolbar toolbar;
    private String placeToSearch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        toolbar = (Toolbar) findViewById(R.id.toolbar_google);
        setSupportActionBar(toolbar);
//        toolbar.

        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            // showCurrentLocation();
            LocationManager locationManager;
            String bestProvider;
            // Get the location manager
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            bestProvider = locationManager.getBestProvider(criteria, false);

            Location location = locationManager
                    .getLastKnownLocation(bestProvider);
            currentLatititude = location.getLatitude();
            currentLongitude = location.getLongitude();
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
                    currentLatititude, currentLongitude));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);

            if (googleMap != null) {
                // adding marker showing users current location
                placeInitialPosition();

            }

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /**
     * This method is for place the Marker in the current LatLang position.
     * <p/>
     * nd naming the current position like 'AppsForBB'.
     */
    private void placeInitialPosition() {
        MarkerOptions markerInitial = new MarkerOptions().position(
                new LatLng(currentLatititude, currentLongitude)).title(
                "AppsForBB");
        markerInitial.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED));
        googleMap.addMarker(markerInitial);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_maptypes, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                placeToSearch = query;
                Toast.makeText(getApplicationContext(), "search " + query, Toast.LENGTH_SHORT).show();
                new AsyncPlace(query).execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    /**
     * This call back is called when the user clicks on the menu item.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hybridmap:
                showHybridMap();
                break;
            case R.id.satelite:
                showSattleiteMap();
                break;
            case R.id.terrain:
                showTerrainMap();
                break;
            case R.id.Normal:
                showNormalMap();
                break;
            case R.id.Traffic:
                showTraffice();
                break;
            case R.id.routeDirections:
//                setOptions();
                break;
            case R.id.Save_map:
                saveMap();

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is to show the traffic on the google map.
     */
    private void showTraffice() {
        googleMap.setTrafficEnabled(true);

    }

    /**
     * show the normal map.
     */
    private void showNormalMap() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    /**
     * Show the Terrain map.
     */
    private void showTerrainMap() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    /**
     * show the satellite map.
     */
    private void showSattleiteMap() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    /**
     * Show the Hybrid map.
     */
    private void showHybridMap() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    /**
     * This method is for saving the map..
     * <p/>
     * to save the google map using 'SnapshotReadyCallback' it gives whole
     * bitmap which was visible on the screen ...
     */
    private void saveMap() {

        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                // TODO Auto-generated method stub
                bitmapMapImage = snapshot;
                saveGraph(bitmapMapImage, GoogleMapActivity.this);
            }
        };
        googleMap.snapshot(callback);
    }

    /**
     * This method is for converting bitmap to the 'ByteArrayOutputStream' and
     * saved it on the external storage.
     *
     * @param graph
     * @param context
     */
    private void saveGraph(Bitmap graph, Context context) {
        Calendar cal = Calendar.getInstance();
        try {
            if (android.os.Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED)) {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Maps");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                @SuppressWarnings("static-access")
                File file = new File(dir, "map_" + cal.getInstance().getTime()
                        + ".png");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                graph.compress(Bitmap.CompressFormat.PNG, 100, baos);
                FileOutputStream f = null;
                f = new FileOutputStream(file);
                if (f != null) {
                    f.write(baos.toByteArray());
                    f.flush();
                    f.close();
                }
            }
            Toast.makeText(GoogleMapActivity.this,
                    "Saved the image succesfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(GoogleMapActivity.this,
                    "exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * AsyncTask is for requesting the url to the server based on the area name
     * what we entered on the edit text.
     *
     * @author android
     */
    private class AsyncPlace extends
            AsyncTask<Integer, Float, List<HashMap<String, String>>> {
        List<HashMap<String, String>> list;
        private ProgressDialog dialog;


        public AsyncPlace(String query) {
            placeToSearch = query;
        }

        @SuppressWarnings("static-access")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*
             * display the progress dialog to the user while loading means
			 * before getting the response from server. MapActivity
			 */
            dialog = new ProgressDialog(GoogleMapActivity.this);
            dialog.setIndeterminate(true);
//            dialog.setIndeterminateDrawable(getResources().getDrawable(
//                    R.anim.progress_dialog_anim));
            dialog.setCancelable(false);
            dialog.setMessage("Loading in, Please wait...!");
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(
                Integer... params) {

            String url = "http://maps.google.com/maps/api/geocode/json?address="
                    + placeToSearch + "&sensor=false";
            JSONObject jsonData = getData(url);
            if (jsonData == null) {
            } else {
                list = AdressJSONParser.parse(jsonData);
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            System.out.println("#############   " + result);
            // after getting response from the server close the progress dialog.
            dialog.cancel();
            googleMap.clear();
            placeInitialPosition();
            setMarkser(result);
        }

    }

    /**
     * This method is for when the user sends the url to the server call this
     * method. this method establishes the connection to the server ang get back
     * the respnse from the server in the form of "HttpResponse"
     * <p/>
     * we can extract the response using the "HttpEntity"
     *
     * @param url
     * @return
     */
    public JSONObject getData(String url) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        StringBuilder bulider = null;
        JSONObject jsnobject = null;
        int i = 0;
        try {
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            bulider = new StringBuilder();
            while ((i = is.read()) != -1) {
                bulider.append((char) i);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            jsnobject = new JSONObject(bulider.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsnobject;
    }

    /**
     * This method is for set the markers on the places at where the
     * location search matches on the entire world.
     *
     * @param result getting the result of list hash map contains place name,
     *               latitude and longitude etc.
     */
    private void setMarkser(List<HashMap<String, String>> result) {
        if (result.size() != 0) {
            Double dlat = null, dlon = null;
            if (result.size() != 0) {
                if (result.size() > 1) {
//                    showDialogToSelectPrefferedPlace(result);
                    // dlat = latlng.latitude;
                    // dlon = latlng.longitude;
                } else {
                    dlat = Double
                            .parseDouble(result.get(0).get("lat") + "");
                    dlon = Double
                            .parseDouble(result.get(0).get("lng") + "");
                    marker = new MarkerOptions().position(
                            new LatLng(dlat, dlon)).title(placeToSearch);
                    BitmapDescriptor markerColor = BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                    marker.icon(markerColor);
                    marker.position(new LatLng(dlat, dlon));
                    googleMap.addMarker(marker);
                    CameraUpdate center = CameraUpdateFactory
                            .newLatLng(new LatLng(dlat, dlon));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
                    googleMap.moveCamera(center);
                    googleMap.animateCamera(zoom);
                }

            }
        } else {
            Toast.makeText(GoogleMapActivity.this, "no search found",
                    Toast.LENGTH_SHORT).show();
        }
    }


}
