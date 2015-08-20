package com.afbb.balakrishna.albumart.maps;

import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdressJSONParser {
	/** Receives a JSONObject and returns a list */
	public static List<HashMap<String, String>> parse(JSONObject jObject) {

		JSONArray jPlaces = null;
		try {
			/** Retrieves all the elements in the 'places' array */
			jPlaces = jObject.getJSONArray("results");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/**
		 * Invoking getPlaces with the array of json object where each json
		 * object represent a place
		 */
		return getPlaces(jPlaces);
	}

	private static List<HashMap<String, String>> getPlaces(JSONArray jPlaces) {
		int placesCount = jPlaces.length();
		List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> place = null;

		/** Taking each place, parses and adds to list object */
		for (int i = 0; i < placesCount; i++) {
			try {
				/** Call getPlace with place JSON object to parse the place */
				place = getPlace((JSONObject) jPlaces.get(i));
				placesList.add(place);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return placesList;
	}

	/**
	 * Parsing the Place JSON object
	 */
	private static HashMap<String, String> getPlace(JSONObject jPlace) {
		String address = "";
		HashMap<String, String> place = new HashMap<String, String>();
		String placeName = "-NA-";
		String vicinity = "-NA-";
		String latitude = "";
		String longitude = "";
		String type = "";
		String address1 = "";
		String address2 = "";
		String city = "";
		String state = "";
		String country = "";
		String county = "";
		String PIN = "";
		String full_address = "";
		String format = "";

		try {
			// Extracting Place name, if available

			latitude = jPlace.getJSONObject("geometry")
					.getJSONObject("location").getString("lat");
			longitude = jPlace.getJSONObject("geometry")
					.getJSONObject("location").getString("lng");
			System.out.println(placeName);
			place.put("lat", latitude);
			place.put("lng", longitude);
			JSONArray address_components = jPlace
					.getJSONArray("address_components");
			format = jPlace.getString("formatted_address");

			for (int i = 0; i < address_components.length(); i++) {
				JSONObject zero2 = address_components.getJSONObject(i);
				String long_name = zero2.getString("long_name");
				JSONArray mtypes = zero2.getJSONArray("types");
				String Type = mtypes.getString(0);

				if (TextUtils.isEmpty(long_name) == false
						|| !long_name.equals(null) || long_name.length() > 0
						|| long_name != "") {
					if (Type.equalsIgnoreCase("street_number")) {

						address1 = long_name + " ";
						address = address1;
					} else if (Type.equalsIgnoreCase("route")) {
						address1 = address1 + long_name;
						address = address1;
					} else if (Type.equalsIgnoreCase("sublocality")) {
						address2 = long_name;
					} else if (Type.equalsIgnoreCase("locality")) {
						// address2 = address2 + long_name + ", ";
						city = long_name;
						address += city;
					} else if (Type
							.equalsIgnoreCase("administrative_area_level_2")) {
						county = long_name;
						address += county;
					} else if (Type
							.equalsIgnoreCase("administrative_area_level_1")) {
						state = long_name;
						address += state;
					} else if (Type.equalsIgnoreCase("country")) {
						country = long_name;
						address += country;
					} else if (Type.equalsIgnoreCase("postal_code")) {
						PIN = long_name;
						address += PIN;
					}
					full_address = address1 + "," + address2 + "," + city + ","
							+ county + "," + state + "," + country + "," + PIN;
				}

			}

			place.put("formatAddress", format);
			while (full_address.startsWith(",")) {
				String tempadd = full_address;
				full_address = "";
				full_address = tempadd.substring(1);
			}
			place.put("addressComponent", full_address);
			place.put("country", country);
			place.put("district", county);
			place.put("city", city);
			place.put("postalcode", PIN);
			place.put("address1", address1);
			place.put("address2", address2);
			place.put("fulladdress", full_address);
		} catch (JSONException e) {
		}
		return place;
	}

	/**
	 * This method is for when the user sends the url to the server call this
	 * method. this method establishes the connection to the server ang get back
	 * the respnse from the server in the form of "HttpResponse"
	 * 
	 * we can extract the response using the "HttpEntity"
	 * 
	 * @param url
	 * @return returns the JSON object of the corrsponding url.
	 */
	public static JSONObject getJSONfromURL(String url) {

		// initialize
		InputStream is = null;
		String result = "";
		JSONObject jObject = null;

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObject = new JSONObject(result);
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		return jObject;
	}

}
