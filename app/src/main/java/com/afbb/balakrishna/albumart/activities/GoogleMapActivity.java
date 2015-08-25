package com.afbb.balakrishna.albumart.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afbb.balakrishna.albumart.R;
import com.afbb.balakrishna.albumart.maps.AdressJSONParser;
import com.afbb.balakrishna.albumart.maps.DirectionsJSONParser;
import com.afbb.balakrishna.albumart.maps.GPSTracker;
import com.afbb.balakrishna.albumart.maps.LatLangJSONParser;
import com.afbb.balakrishna.albumart.maps.MapUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GoogleMapActivity extends AppCompatActivity implements LocationListener {
    // Google Map
    private GoogleMap googleMap;
    private double currentLatititude;
    private double currentLongitude;
    private Bitmap bitmapMapImage;
    private MarkerOptions marker;
    private Toolbar toolbar;
    private String placeToSearch = "";
    private SupportMapFragment supportMapFragment;
    private String[] strModes;
    private String[] strRanges;
    private String modeSelected;
    private int rangeSelected;
    private String strPlaceType;
    private BitmapDescriptor markerColor;
    private MyAsync async;
    private DownloadTask downloadTask;
    private ParserTask parserTask;

    private String mMapMode = "driving";
    private int mMapRange = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        toolbar = (Toolbar) findViewById(R.id.toolbar_google);
        setSupportActionBar(toolbar);
        supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!MapUtils.getInstance(this).isGooglePlayServicesAvailable()) {
            finish();
        }
        checkGPS();
        googleMap = supportMapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

        /**
         * Listner is called,When the user clicks on the marker.
         */
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            // get the corrsponding marker reference.
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng position = marker.getPosition();
                /*
                 * while set the markers it self , set the title and
				 * position(LatLang) to each marker ,when the user click on the
				 * marker we can able to get all the data belonging to that
				 * marker.
				 */
                String placeName = marker.getTitle();
                double distance = MapUtils.CalculationByDistance(currentLatititude, currentLongitude, position.latitude,
                        position.longitude);
                String dist = distance + "";
                String strResDistance = dist.substring(0, 4);
                Toast.makeText(GoogleMapActivity.this,
                        "Distance: " + strResDistance + " Kms",
                        Toast.LENGTH_SHORT).show();
                showDialogForRouteConfirmation(strResDistance, position,
                        placeName);
                return false;
            }

            /**
             * After click on the marker visible one dialog to the user like
             * whether he want route or not if yes then only find the path..
             *
             * @param strResDistance
             *            shows the how much distance to draw the route on the
             *            dialog..
             * @param position
             *            clicked marker latLang position.
             * @param placeName
             *            visible place name t o on the dialog.
             */
            private void showDialogForRouteConfirmation(String strResDistance,
                                                        final LatLng position, String placeName) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(
                        GoogleMapActivity.this);
                dialog.setTitle(placeName + " [distance: " + strResDistance
                        + " km ]");
                dialog.setMessage("Do you want route :)");
                dialog.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                drawRoute(position.latitude, position.longitude);
                            }
                        });
                dialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        });
                dialog.show();

            }
        });


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


    public void checkGPS() {
        GPSTracker gps = new GPSTracker(GoogleMapActivity.this);

        //hi this is bala krishna and i am working in applica

        // check if GPS enabled
        if (gps.canGetLocation()) {

            currentLatititude = gps.getLatitude();
            currentLongitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + currentLatititude + "\nLong: " + currentLongitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    /**
     * this method is for,When the user want to know the route between two
     * LatLang positions. ( using the directions api )
     *
     * @param dlat
     * @param dlon
     */
    private void drawRoute(Double dlat, Double dlon) {
        LatLng latLngDest = new LatLng(dlat, dlon);
        LatLng latLngSource = new LatLng(currentLatititude, currentLongitude);
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(latLngSource, latLngDest);
        downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

    }

    /**
     * This method just returns the direction url based on the latitude and
     * longitude what we passed as parameters.
     *
     * @param origin
     * @param dest
     * @return
     */
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + ","
                + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + parameters;

        return url;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(GoogleMapActivity.this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage("Route finding .. ");
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        private String distance;
        private String duration;

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    if (j == 0) { // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(4);
                lineOptions.color(Color.RED);
//                Toast.makeText(getApplicationContext(), "time: " + duration + " distance: " + distance, Toast.LENGTH_SHORT).show();
            }

            // Drawing polyline in the Google Map for the i-th route
            googleMap.addPolyline(lineOptions);
        }
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("err downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
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
        MenuItem item_spiner = menu.findItem(R.id.action_spinner_placetype);
        Spinner spinner = (Spinner) item_spiner.getActionView();
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, MapUtils.getListOfPlaces());
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {

                cancelBackgroundRunningThreadyIfAny();
                Toast.makeText(GoogleMapActivity.this,
                        "Range: " + mMapRange + "  mode:" + mMapMode,
                        Toast.LENGTH_SHORT).show();
                googleMap.clear();
                placeInitialPosition();
                strPlaceType = MapUtils.getListOfPlaces()[position];
                getNearestPlaces(strPlaceType);
                markerColor = getRandomIcon(new Random().nextInt(10));

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    /**
     * This method is for cancel the background running tasks, like when if we
     * select one item like 'Hospitals' and before getting response if we click
     * on any other item like 'Theaters' before asynchronous task should be
     * cancel.
     */
    private void cancelBackgroundRunningThreadyIfAny() {
        if (async != null && async.getStatus() == AsyncTask.Status.RUNNING) {
            // My AsyncTask is currently doing work in
            // doInBackground()
            async.cancel(true);
        }
        if (downloadTask != null
                && downloadTask.getStatus() == AsyncTask.Status.RUNNING) {
            downloadTask.cancel(true);
        }
        if (parserTask != null
                && parserTask.getStatus() == AsyncTask.Status.RUNNING) {
            parserTask.cancel(true);
        }

    }

    /**
     * This method is for getting the nearest places of the particular palce.
     *
     * @param strPlaceType
     */
    private void getNearestPlaces(String strPlaceType) {
        if (!strPlaceType.equals("none")) {
            if (isNetWorkAvailable()) {
                async = new MyAsync();
                async.execute();
            } else {
                Toast.makeText(GoogleMapActivity.this,
                        "No NetWork Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This method is just checking whether the network is available or not if
     * not available. if available return true other wise just return false.
     *
     * @return
     */
    public boolean isNetWorkAvailable() {

        ConnectivityManager check = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (check != null) {
            NetworkInfo[] info = check.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        } else {
            Toast.makeText(this, "not conencted to internet",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;

    }

    /**
     * This async task is for when the user sends the url to the server
     * processing the url in the doInBackground of the current async task.
     *
     * @author android
     */
    private class MyAsync extends
            AsyncTask<Integer, Float, List<HashMap<String, String>>> {
        ProgressDialog dialog;
        private List<HashMap<String, String>> list;
        String url;

        /*
         * up to getting the response from the server we just show dialog like
         * "Loading... " (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(GoogleMapActivity.this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage("Loading in, Please wait...!");
            dialog.setCanceledOnTouchOutside(true);
//			dialog.show(GoogleMapsActivity.this, "",
//					"Loading in, Please wait...!", true, true,
//					new DialogInterface.OnCancelListener() {
//
//						@Override
//						public void onCancel(DialogInterface dialog) {
//
//						}
//					});
            dialog.show();

        }

        @Override
        protected List<HashMap<String, String>> doInBackground(
                Integer... params) {
            if (!strPlaceType.equals("none")) {
                if (!strPlaceType.equals("all")) {
                    url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                            + currentLatititude
                            + ","
                            + currentLongitude
                            + "&radius="
                            + mMapRange
                            + "&types="
                            + strPlaceType
                            + "&key=AIzaSyCM30efnC8vQXoOMI16xk-DzBJKa9v22oA&sensor=true&mode="
                            + mMapMode;
                    Log.d("MyAsync", "doInBackground 620 " + url);
                } else {
                    url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                            + currentLatititude
                            + ","
                            + currentLongitude
                            + "&radius="
                            + mMapRange
                            + "&key=AIzaSyCM30efnC8vQXoOMI16xk-DzBJKa9v22oA&sensor=true&mode="
                            + mMapMode;
                }
            }
            JSONObject jsonData = getData(url);
            if (jsonData == null) {
                return null;
            } else {
                list = LatLangJSONParser.parse(jsonData);
                return list;
            }

        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            Log.d("MyAsync", "onPostExecute 644 "+  result);
            if (result.size() == 0) {

                Toast.makeText(
                        GoogleMapActivity.this,
                        strPlaceType + "s are not available with in ur range .",
                        Toast.LENGTH_SHORT).show();
                dialog.cancel();

            } else {
                System.out.println(result);
                dialog.cancel();
                setMarksers(result);
            }

        }
    }

    /**
     * After geting the response we can get the data and maintain in the list
     * format.
     * <p/>
     * in the LatLang positions of the entire list we can place the markers
     * using this method..
     *
     * @param result
     */
    private void setMarksers(List<HashMap<String, String>> result) {
        Double dlat, dlon;
        for (HashMap<String, String> hashMap : result) {
            dlat = Double.parseDouble(hashMap.get("lat") + "");
            dlon = Double.parseDouble(hashMap.get("lng") + "");
            marker = new MarkerOptions().position(new LatLng(dlat, dlon))
                    .title(hashMap.get("place_name"));
            marker.icon(markerColor);
            marker.position(new LatLng(dlat, dlon));
            googleMap.addMarker(marker);
            // drawRoute(dlat, dlon);

        }

    }

    /**
     * This method is to set the marker color based on the integer value.
     *
     * @param i
     * @return
     */
    private BitmapDescriptor getRandomIcon(int i) {
        switch (i) {
            // when i is 0
            case 0:
                markerColor = BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                break;
            // when i is 1
            case 1:
                markerColor = BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                break;
            // when i is 2
            case 2:
                markerColor = BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
                break;
            // when i is 4
            case 4:
                markerColor = BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                break;
            // when i is 5
            case 5:
                markerColor = BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                break;
            // when i is 6
            case 6:
                markerColor = BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                break;
            // when i is 7
            case 7:
                markerColor = BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                break;
            // when i is 8
            case 8:
                markerColor = BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ROSE);
                break;
            // when i is 9
            case 9:
                markerColor = BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
                break;
            // when i is 10
            case 10:
                markerColor = BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
                break;
            default:
                break;
        }
        return markerColor;

    }

    @Override
    public void onLocationChanged(Location location) {
        TextView locationTv = (TextView) findViewById(R.id.latlongLocation);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        Toast.makeText(getApplicationContext(), "Latitude:" + latitude + ", Longitude:" + longitude, Toast.LENGTH_SHORT).show();
//        locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);
        placeInitialPosition();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
                setOptions();
                break;
            case R.id.Save_map:
                saveMap();


            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setOptions() {
        Spinner spinnerMode = null;
        strModes = new String[]{"driving", "walking", "bicycling"};
        strRanges = new String[]{"0.5 kms", "1 kms", "1.5 kms", "2 kms",
                "2.5 kms", "3 kms"};

        final AlertDialog dialog = new AlertDialog.Builder(
                GoogleMapActivity.this).create();
        View view = getLayoutInflater().inflate(R.layout.extrafeature, null);
        spinnerMode = (Spinner) view.findViewById(R.id.spinnerMODE);
        Spinner spinnerRange = (Spinner) view.findViewById(R.id.spinnerRange);
        Button buttonOK = (Button) view.findViewById(R.id.buttonOK);
        Button buttonCancel = (Button) view.findViewById(R.id.buttonCancel);
        ArrayAdapter<String> adapterModes = new ArrayAdapter<String>(
                GoogleMapActivity.this, android.R.layout.simple_list_item_1,
                strModes);
        ArrayAdapter<String> adapterRanges = new ArrayAdapter<String>(
                GoogleMapActivity.this, android.R.layout.simple_list_item_1,
                strRanges);
        spinnerMode.setAdapter(adapterModes);
        spinnerRange.setAdapter(adapterRanges);
        dialog.setView(view);
        dialog.show();

        spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                modeSelected = strModes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spinnerRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {

                rangeSelected = getRange(strRanges[position]);

            }

            /**
             * Based on the user selection (range parameter) it returns the int
             * value in the meters.
             *
             * @param range
             * @return
             */
            private int getRange(String range) {
                if (range.equalsIgnoreCase("0.5 kms"))
                    return 500;
                else if (range.equalsIgnoreCase("1 kms"))
                    return 1000;
                else if (range.equalsIgnoreCase("1.5 kms"))
                    return 1500;
                else if (range.equalsIgnoreCase("2 kms"))
                    return 2000;
                else if (range.equalsIgnoreCase("2.5 kms"))
                    return 2500;
                else if (range.equalsIgnoreCase("3 kms"))
                    return 3000;

                return 3000;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        buttonOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(GoogleMapActivity.this,
                        "Successfully set options.", Toast.LENGTH_SHORT).show();
                mMapMode = modeSelected;
                mMapRange = rangeSelected;
                dialog.cancel();
                googleMap.clear();
                getNearestPlaces(strPlaceType);
                placeInitialPosition();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

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


}
