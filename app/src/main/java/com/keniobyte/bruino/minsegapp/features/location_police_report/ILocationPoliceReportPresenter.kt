package com.keniobyte.bruino.minsegapp.features.location_police_report

import android.widget.AdapterView

import com.google.android.gms.maps.model.LatLng
import com.keniobyte.bruino.minsegapp.utils.view_presenter.Presenter
import com.keniobyte.bruino.minsegapp.utils.view_presenter.View

import java.io.UnsupportedEncodingException
import java.util.ArrayList

/**
 * @author bruino
 * *
 * @version 02/01/17.
 */

interface ILocationPoliceReportPresenter<T: View>: Presenter<T>{
    fun onPlaceSelected(latLng: LatLng)
    fun nextStep()
    fun onMapClick(latLng: LatLng)
    fun unregisterReceiver()
}
