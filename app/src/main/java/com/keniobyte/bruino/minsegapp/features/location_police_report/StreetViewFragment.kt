package com.keniobyte.bruino.minsegapp.features.location_police_report

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback
import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.StreetViewPanoramaCamera
import com.keniobyte.bruino.minsegapp.R

/**
 * @author bruino
 * @version 29/06/17.
 */

class StreetViewFragment: Fragment(), OnStreetViewPanoramaReadyCallback, IUpdateableStreetView {
    private var listener: OnChangedPositionStreetView? = null
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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = activity as OnChangedPositionStreetView
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_street_view, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null && (lat != arguments.getDouble(ARG_PARAM1) || lng != arguments.getDouble(ARG_PARAM2))) {
            lat = arguments.getDouble(ARG_PARAM1)
            lng = arguments.getDouble(ARG_PARAM2)
        }

        var streetViewPanoramaFragment: SupportStreetViewPanoramaFragment? = childFragmentManager.findFragmentById(R.id.street_view_panorama) as SupportStreetViewPanoramaFragment?
        if (streetViewPanoramaFragment == null) {
            streetViewPanoramaFragment = SupportStreetViewPanoramaFragment.newInstance()
            childFragmentManager.beginTransaction().replace(R.id.street_view_panorama, streetViewPanoramaFragment, "street_view").commit()
        }
        streetViewPanoramaFragment!!.getStreetViewPanoramaAsync(this)
    }

    override fun onStreetViewPanoramaReady(streetViewPanorama: StreetViewPanorama?) {
        streetview = streetViewPanorama
        streetview?.apply {
            isUserNavigationEnabled = true
            isStreetNamesEnabled = true
            setOnStreetViewPanoramaChangeListener {
                listener?.onChangedLatLngStreetView(it.position) }
            setOnStreetViewPanoramaCameraChangeListener {
                listener?.onChangedCameraStreetView(it.bearing, it.tilt)
            }
        }
    }

    override fun update(data: LatLng) {
        if (streetview?.location?.position != data) {
            streetview?.setPosition(data)
            val camera = StreetViewPanoramaCamera.Builder(streetview!!.panoramaCamera)
                    .tilt(0F)
                    .bearing(0F)
                    .build()
            streetview?.animateTo(camera, 0)
        }

    }

    interface OnChangedPositionStreetView {
        fun onChangedLatLngStreetView(latLng: LatLng)
        fun onChangedCameraStreetView(bearing: Float, tilt: Float)

    }
}
