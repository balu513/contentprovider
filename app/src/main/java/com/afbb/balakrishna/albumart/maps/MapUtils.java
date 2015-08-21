package com.afbb.balakrishna.albumart.maps;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

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
}