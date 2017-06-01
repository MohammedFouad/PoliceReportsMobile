package com.keniobyte.bruino.minsegapp.features.location_police_report

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

/**
 * @author bruino
 * @version 31/05/17.
 */
class MapFragment: SupportMapFragment(), OnMapReadyCallback {
    companion object {
        val LAT = -26.8167
        val LNG = -65.2167

        //Zone: San Miguel de Tucuman, Tucuman, Argentina
        val LAT_NORTHEAST = -26.142496
        val LNG_NORTHEAST = -64.460569
        val LAT_SOUTHWEST = -28.013720
        val LNG_SOUTHWEST = -66.183524
    }
    var map: GoogleMap? = null

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        map?.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(LAT, LNG), 11f))
            uiSettings.isZoomControlsEnabled = true
            //setOnMapClickListener { presenter.onMapClick(it) }
        }
    }
}