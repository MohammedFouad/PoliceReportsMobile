package com.keniobyte.bruino.minsegapp.features.location_police_report

import com.google.android.gms.maps.model.LatLng
import com.keniobyte.bruino.minsegapp.utils.view_presenter.Presenter
import com.keniobyte.bruino.minsegapp.utils.view_presenter.View

/**
 * @author bruino
 * @version 02/01/17.
 */

interface ILocationPoliceReportPresenter<T: View>: Presenter<T>{
    fun geocodingReverse(latLng: LatLng)
    fun unregisterReceiver()
    fun nextStep()
}
