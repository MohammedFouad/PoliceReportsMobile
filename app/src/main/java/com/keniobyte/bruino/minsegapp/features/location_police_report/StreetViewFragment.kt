package com.keniobyte.bruino.minsegapp.features.location_police_report

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback
import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment
import com.google.android.gms.maps.model.LatLng
import com.keniobyte.bruino.minsegapp.R

/**
 * @author bruino
 * @version 31/05/17.
 */

class StreetViewFragment: Fragment(), OnStreetViewPanoramaReadyCallback {
    var streetview: StreetViewPanorama? = null
    private val ARG_PARAM1 = "lat"
    private val ARG_PARAM2 = "lng"
    private var lat: Double? = null
    private var lng: Double? = null

    fun newInstance(lat: Double, lng: Double): StreetViewFragment {
        val fragment = StreetViewFragment()
        val args = Bundle()
        args.putDouble(ARG_PARAM1, lat)
        args.putDouble(ARG_PARAM2, lng)
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_street_view, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            lat = arguments.getDouble(ARG_PARAM1)
            lng = arguments.getDouble(ARG_PARAM2)
        }

        var streetViewPanoramaFragment: SupportStreetViewPanoramaFragment? = childFragmentManager.findFragmentById(R.id.street_view_panorama) as SupportStreetViewPanoramaFragment?
        if (streetViewPanoramaFragment == null) {
            streetViewPanoramaFragment = SupportStreetViewPanoramaFragment.newInstance()
            childFragmentManager.beginTransaction().replace(R.id.street_view_panorama, streetViewPanoramaFragment).commit()
        }
        streetViewPanoramaFragment!!.getStreetViewPanoramaAsync(this)
    }

    override fun onStreetViewPanoramaReady(streetViewPanorama: StreetViewPanorama?) {
        streetview = streetViewPanorama
        streetview?.apply{
            if (lat != null && lng != null) setPosition(LatLng(lat!!, lng!!))
            isUserNavigationEnabled = true
            isStreetNamesEnabled = true
        }
    }
}