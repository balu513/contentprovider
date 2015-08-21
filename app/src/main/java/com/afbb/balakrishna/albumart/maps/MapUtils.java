package com.afbb.balakrishna.albumart.maps;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.text.DecimalFormat;

/**
 * Created by balakrishna on 21/8/15.
 */
public class MapUtils {

    public static MapUtils instance;
    private Context context;

    public MapUtils(Context context) {
        this.context = context;
    }

    public static MapUtils getInstance(Context context) {
        if (instance == null)
            instance = new MapUtils(context);

        return instance;
    }


    public boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * These are all the place types which are supported by the google places
     * api..
     * <p/>
     * These places are displayed in the spinner when the user clickes on the
     * place type ..we can find the places around the particicular LatLang of
     * the regording type.
     * <p/>
     * let, if we click on the 'cafe' (int the url placetype is 'cafe') then it
     * responds returns places only of type Cafe.
     *
     * @return
     */
    public static String[] getListOfPlaces() {
        String[] placetypes = new String[]{"none", "all", "airport", "atm",

                "aquarium", "accounting", "airport", "amusement_park", "art_gallery",
                "amusement_park", "bakery", "bank", "bar", "beauty_salon",
                "bicycle_store", "book_store", "bowling_alley", "bus_station",
                "car_rental", "cafe", "campground", "car_dealer", "car_rental",
                "car_repair", "car_wash", "casino", "cemetery", "church",
                "city_hall", "courthouse", "convenience_store",
                "clothing_store", "department_store", "dentist", "doctor",
                "electronics_store", "electrician", "embassy", "establishment",
                "finance", "fire_station", "florist", "food", "funeral_home",
                "furniture_store", "gas_station", "general_contractor",
                "grocery_or_supermarket", "grocery_or_supermarket", "gym",
                "hair_care", "hardware_store", "health", "hindu_temple",
                "hospital", "home_goods_store", "insurance_agency",
                "jewelry_store", "laundry", "lawyer", "library",
                "liquor_store", "local_government_office", "locksmith",
                "lodging", "meal_delivery", "meal_takeaway", "movie_rental",
                "movie_theater", "moving_company", "mosque", "museum",
                "night_club", "painter", "parking", "park", "pet_store",
                "pharmacy", "physiotherapist", "place_of_worship", "police",
                "plumber", "post_office", "real_estate_agency", "restaurant",
                "roofing_contractor", "rv_park", "school", "shoe_store",
                "shopping_mall", "spa", "stadium", "storage", "store",
                "subway_station", "synagogue", "taxi_stand", "train_station",
                "university", "veterinary_care", "zoo"};
        return placetypes;
    }

    /**
     * This method is for calculating the distance between the two LatLang
     * positions.
     * <p/>
     * using this method only we can show the how long distance to the user...
     *
     * @param destLat
     * @param destLong
     * @return
     */
    public static double CalculationByDistance(Double sourceLat, Double sourceLang, Double destLat, Double destLong) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = sourceLat;
        double lat2 = destLat;
        double lon1 = sourceLang;
        double lon2 = destLong;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        Integer kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        Integer meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }
}