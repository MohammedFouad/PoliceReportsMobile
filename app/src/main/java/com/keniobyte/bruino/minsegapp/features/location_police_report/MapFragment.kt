package com.keniobyte.bruino.minsegapp.features.location_police_report

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.keniobyte.bruino.minsegapp.R

/**
 * @author bruino
 * @version 16/06/17.
 */
class MapFragment: Fragment(), OnMapReadyCallback, IUpdateAddressPlaceAutocomplete
        , IUpdateLocationMapFragment {
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
    var placeAutocompleteFragment: SupportPlaceAutocompleteFragment? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = activity as OnPositionSelectedListener
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_map_container, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var mapFragment: SupportMapFragment? = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
        placeAutocompleteFragment = childFragmentManager.findFragmentById(R.id.place_autocomplete) as SupportPlaceAutocompleteFragment?
        placeAutocompleteFragment?.apply {
            setBoundsBias(LatLngBounds(
                    LatLng(LAT_SOUTHWEST, LNG_SOUTHWEST),
                    LatLng(LAT_NORTHEAST, LNG_NORTHEAST)))

            setFilter(AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .setCountry("AR")
                    .build())

            setOnPlaceSelectedListener(object : PlaceSelectionListener {
                override fun onPlaceSelected(place: Place) {
                    map?.clear()
                    map?.addMarker(MarkerOptions().position(place.latLng))
                    listener!!.onPositionSelected(place.latLng)
                }

                override fun onError(p0: Status?) {
                    //reverseGeocodingMessageError()
                }
            })
        }

        if (mapFragment == null) {
            mapFragment = SupportMapFragment()
            childFragmentManager.beginTransaction().replace(R.id.map_fragment_container, mapFragment).commit()
        }
        mapFragment.getMapAsync(this)

        if (placeAutocompleteFragment == null) {
            placeAutocompleteFragment = SupportPlaceAutocompleteFragment()
            placeAutocompleteFragment?.apply {
                setBoundsBias(LatLngBounds(
                        LatLng(LAT_SOUTHWEST, LNG_SOUTHWEST),
                        LatLng(LAT_NORTHEAST, LNG_NORTHEAST)))

                setFilter(AutocompleteFilter.Builder()
                        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                        .setCountry("AR")
                        .build())

                setOnPlaceSelectedListener(object :PlaceSelectionListener {
                    override fun onPlaceSelected(place: Place) {
                        map?.clear()
                        map?.addMarker(MarkerOptions().position(place.latLng))
                        listener!!.onPositionSelected(place.latLng)
                    }

                    override fun onError(p0: Status?) {
                        //reverseGeocodingMessageError()
                    }
                })
            }
            childFragmentManager.beginTransaction().replace(R.id.card_view, placeAutocompleteFragment).commit()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(LAT, LNG), 11f))
            uiSettings.isZoomControlsEnabled = false
            setOnMapClickListener {
                clear()
                addMarker(MarkerOptions().position(it))
                listener!!.onPositionSelected(it)
            }
        }
    }

    override fun update(address: String?) {
        if (address != null) placeAutocompleteFragment?.setText(address)
    }

    override fun update(latLng: LatLng) {
        map?.apply {
            clear()
            addMarker(MarkerOptions().position(latLng))
        }
    }

    interface OnPositionSelectedListener {
        fun onPositionSelected(latLng: LatLng)
    }
}