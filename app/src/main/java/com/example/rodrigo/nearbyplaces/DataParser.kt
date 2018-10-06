package com.example.rodrigo.nearbyplaces

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class DataParser {

    private fun getPlace(googlePlaceJson:JSONObject):HashMap<String,String>{

        var googlePlacesMap = HashMap<String,String>()
        var placeName = "--NA--"
        var vicinity = "--NA--"
        var latitude = ""
        var longitude = ""
        var reference = ""

        try {
            if(!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name")
            }
            if(!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity")
            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat")
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng")
            reference = googlePlaceJson.getString("reference")

            googlePlacesMap.put("place_name", placeName)
            googlePlacesMap.put("vicinity", vicinity)
            googlePlacesMap.put("lat", latitude)
            googlePlacesMap.put("lng", longitude)
            googlePlacesMap.put("reference", reference)
        }
        catch (e:IOException){
            e.printStackTrace()
        }

        return googlePlacesMap
    }

    private fun getPlaces(jsonArray: JSONArray):List<HashMap<String,String>>{

        var count:Int = jsonArray.length()
        var placesList = ArrayList<HashMap<String,String>>()
        var placeMap: HashMap<String,String>

        for (i in 0 until count){
            try {
                placeMap = getPlace(jsonArray.get(i) as JSONObject)
                placesList.add(placeMap)
            }
            catch (e:JSONException){
                e.printStackTrace()
            }
        }
        return placesList
    }

    fun parse(jsonData: String):List<HashMap<String,String>>{

        var jsonArray:JSONArray? = null
        var jsonObject:JSONObject?

        try{
            jsonObject = JSONObject(jsonData)
            jsonArray = jsonObject.getJSONArray("results")
        }
        catch (e:JSONException) {
            e.printStackTrace()
        }

        return getPlaces(jsonArray!!)
    }
}