package com.keniobyte.bruino.minsegapp.features.location_police_report

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.keniobyte.bruino.minsegapp.R

/**
 * @author bruino
 * @version 31/05/17.
 */
class MapFragment: Fragment(), OnMapReadyCallback {
    companion object {
        val LAT = -26.8167
        val LNG = -65.2167

        //Zone: San Miguel de Tucuman, Tucuman, Argentina
        val LAT_NORTHEAST = -26.142496
        val LNG_NORTHEAST = -64.460569
        val LAT_SOUTHWEST = -28.013720
        val LNG_SOUTHWEST = -66.183524
    }
    private var listener: OnPositionSelectedListener? = null
    var map: GoogleMap? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = activity as OnPositionSelectedListener
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_map, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var mapFragment: SupportMapFragment? = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
        if (mapFragment == null) {
            mapFragment = SupportMapFragment()
            childFragmentManager.beginTransaction().replace(R.id.map_fragment, mapFragment).commit()
        }
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(LAT, LNG), 11f))
            uiSettings.isZoomControlsEnabled = true
            setOnMapClickListener {
                clear()
                addMarker(MarkerOptions().position(it))
                listener!!.onPositionSelected(it)
            }
        }
    }



    interface OnPositionSelectedListener {
        fun onPositionSelected(latLng: LatLng)
    }
}