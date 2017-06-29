package com.keniobyte.bruino.minsegapp.features.location_police_report

import com.google.android.gms.maps.model.LatLng
import com.keniobyte.bruino.minsegapp.utils.view_presenter.View

/**
 * @author bruino
 * @version 22/04/17.
 */

interface ILocationPoliceReportView: View {
    fun setEnableNextStepButton(i: Boolean)
    fun setLocationPoliceReport(location: LatLng)
    fun setTextPlaceAutocomplete(address: String)
    fun navigationToPoliceReportActivity()
    fun reverseGeocodingMessageError()
}
