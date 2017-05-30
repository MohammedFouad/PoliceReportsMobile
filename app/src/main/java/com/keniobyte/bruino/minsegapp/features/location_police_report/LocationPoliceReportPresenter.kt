package com.keniobyte.bruino.minsegapp.features.location_police_report

import android.location.Location
import android.location.LocationManager
import com.google.android.gms.maps.model.LatLng
import com.keniobyte.bruino.minsegapp.App
import io.nlopez.smartlocation.SmartLocation

/**
 * @author bruino
 * @version 22/05/17.
 */

class LocationPoliceReportPresenter(override var view: ILocationPoliceReportView?) : ILocationPoliceReportPresenter<ILocationPoliceReportView> {
    private val PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place"
    private val TYPE_AUTOCOMPLETE = "/autocomplete"
    private val OUT_JSON = "/json"

    override fun onPlaceSelected(latLng: LatLng) {
        view?.addMarkerInGoogleMap(latLng)
        view?.setLocationPoliceReport(latLng)
        view?.setEnableNextStepButton(true)
    }

    override fun nextStep() {
        view?.navigationToPoliceReportActivity()
    }

    override fun onMapClick(latLng: LatLng) {
        view?.apply {
            addMarkerInGoogleMap(latLng)
            setEnableNextStepButton(false)
            showProgressBar()
        }

        val location = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = latLng.latitude
            longitude = latLng.longitude
        }

        SmartLocation.with(App.instance).geocoding().reverse(location) { _, list ->
            if (list.size > 0) {
                val address = list[0].getAddressLine(0)
                view?.apply {
                    hideProgressBar()
                    setTextPlaceAutocomplete(address)
                    setLocationPoliceReport(latLng)
                    setEnableNextStepButton(true)
                }
            } else {
                view?.apply {
                    reverseGeocodingMessageError()
                    hideProgressBar()
                }
            }
        }

    }

    override fun unregisterReceiver() {
        SmartLocation.with(App.instance).geocoding().stop()
    }

}