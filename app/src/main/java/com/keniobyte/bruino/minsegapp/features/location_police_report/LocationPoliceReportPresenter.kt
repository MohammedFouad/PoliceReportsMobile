package com.keniobyte.bruino.minsegapp.features.location_police_report

import android.location.Location
import android.location.LocationManager
import com.google.android.gms.maps.model.LatLng
import com.keniobyte.bruino.minsegapp.App
import io.nlopez.smartlocation.SmartLocation

/**
 * @author bruino
 * @version 16/06/17.
 */

class LocationPoliceReportPresenter(override var view: ILocationPoliceReportView?) : ILocationPoliceReportPresenter<ILocationPoliceReportView> {
    override fun geocodingReverse(latLng: LatLng) {
        view?.setEnableNextStepButton(false)

        val location = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = latLng.latitude
            longitude = latLng.longitude
        }

        SmartLocation.with(App.instance).geocoding().reverse(location) { _, list ->
            if (list.size > 0) {
                val address = list[0].getAddressLine(0)
                view?.apply {
                    setTextPlaceAutocomplete(address)
                    setEnableNextStepButton(true)
                }
            } else {
                view?.apply {
                    reverseGeocodingMessageError()
                }
            }
        }
    }

    override fun unregisterReceiver() {
        SmartLocation.with(App.instance).geocoding().stop()
    }

    override fun nextStep() {
        view?.navigationToPoliceReportActivity()
    }
}
