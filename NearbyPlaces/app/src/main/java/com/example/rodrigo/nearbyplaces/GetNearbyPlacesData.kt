package com.example.rodrigo.nearbyplaces

import android.os.AsyncTask
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class GetNearbyPlacesData:AsyncTask<Any,String,String>(){

    var googlePlacesData:String? = null
    var mMap:GoogleMap? = null
    var url:String? = null

    override fun doInBackground(vararg objects:Any): String {
        mMap = objects[0] as GoogleMap
        url = objects[1] as String

        var downloadURL = DownloadURL()
        try{
            googlePlacesData = downloadURL.readUrl(url!!)
        }
        catch (e:IOException){
            e.printStackTrace()
        }

        return googlePlacesData!!
    }

    override fun onPostExecute(result: String) {
        var nearbyPlaceList: List<HashMap<String,String>>
        var parser = DataParser()
        nearbyPlaceList = parser.parse(result)
        showNearbyPlaces(nearbyPlaceList)
    }

    private fun showNearbyPlaces(nearbyPlaceList: List<HashMap<String,String>>){

        for (i in 0 until nearbyPlaceList.size){
            var markerOptions = MarkerOptions()
            var googlePlace = nearbyPlaceList.get(i)

            var placeName = googlePlace.get("place_name")
            var vicinity = googlePlace.get("vicinity")
            var lat = java.lang.Double.parseDouble(googlePlace.get("lat"))
            var lng = java.lang.Double.parseDouble(googlePlace.get("lng"))

            var latLng = LatLng(lat,lng)
            markerOptions.position(latLng)
            markerOptions.title(placeName + " : " + vicinity)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

            mMap!!.addMarker(markerOptions)
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(10f))
        }
    }
}