package com.afbb.balakrishna.albumart.maps;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PlaceJSONParser {

	/**
	 * Receives a JSONObject and returns a list
	 * 
	 * @throws org.json.JSONException
	 */
	public static ArrayList<HashMap> parse(JSONObject jObject) {
		ArrayList<HashMap> list = new ArrayList<HashMap>();
		HashMap hashmap = null;
		try {
			JSONArray jsonArry = jObject.getJSONArray("results");
			for (int i = 0; i < jsonArry.length(); i++) {
				JSONObject jsonObjectResults = jsonArry.getJSONObject(i);
				JSONArray jsonArray_addressComponents = jsonObjectResults
						.getJSONArray("address_components");
				JSONObject jsonObject_addr1 = jsonArray_addressComponents
						.getJSONObject(0);
				String strvilgName = jsonObject_addr1.getString("long_name");
				String straddress = jsonObjectResults
						.getString("formatted_address");
				JSONObject jsonObjectGeometry = jsonObjectResults
						.getJSONObject("geometry");

				JSONObject jsonObjectLocation = jsonObjectGeometry
						.getJSONObject("location");
				double lat = jsonObjectLocation.getDouble("lat");
				double lng = jsonObjectLocation.getDouble("lng");
				hashmap = new HashMap();
				hashmap.put("long_name", strvilgName);
				hashmap.put("formatted_address", straddress);
				hashmap.put("latitude", lat);
				hashmap.put("longitude", lng);
				list.add(hashmap);

			}

		} catch (Exception e) {

		}

		return list;

	}

}
