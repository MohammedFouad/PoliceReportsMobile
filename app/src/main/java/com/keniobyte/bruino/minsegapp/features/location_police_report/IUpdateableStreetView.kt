package com.keniobyte.bruino.minsegapp.features.location_police_report

import com.google.android.gms.maps.model.LatLng

/**
 * @author bruino
 * @version 06/06/17.
 */

interface IUpdateableStreetView {
    fun update(data: LatLng)
}