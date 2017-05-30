package com.keniobyte.bruino.minsegapp.features.location_police_report

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.keniobyte.bruino.minsegapp.R
import com.keniobyte.bruino.minsegapp.features.police_report.PoliceReportActivity
import com.keniobyte.bruino.minsegapp.models.PoliceReport
import kotlinx.android.synthetic.main.police_report_activity_location.*
import org.jetbrains.anko.alert

class LocationPoliceReportActivity : AppCompatActivity(), ILocationPoliceReportView, OnMapReadyCallback {
    companion object {
        val LAT = -26.8167
        val LNG = -65.2167

        //Zone: San Miguel de Tucuman, Tucuman, Argentina
        val LAT_NORTHEAST = -26.142496
        val LNG_NORTHEAST = -64.460569
        val LAT_SOUTHWEST = -28.013720
        val LNG_SOUTHWEST = -66.183524
    }

    private val presenter: ILocationPoliceReportPresenter<ILocationPoliceReportView> by lazy { LocationPoliceReportPresenter(this) }
    private var locationPoliceReport: LatLng? = null

    var map: GoogleMap? = null
    val placeAutocompleteFragment: PlaceAutocompleteFragment? by lazy {
        fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment?
    }

    private var address: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.police_report_activity_location)

        //Address Autocomplete
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
                    Log.i(javaClass.simpleName, place.latLng.toString())
                    presenter.onPlaceSelected(place.latLng)
                    address = place.address.toString()
                }

                override fun onError(p0: Status?) {
                    reverseGeocodingMessageError()
                }
            })
        }

        if (map == null){
            val mapfragment = fragmentManager.findFragmentById(R.id.googleMap) as MapFragment
            mapfragment.getMapAsync(this)
        }

        setEnableNextStepButton(false)
        nextStepButton.setOnClickListener { presenter.nextStep() }

        setTitleToolbar(getTypePoliceReport())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(LAT, LNG), 11f))
            uiSettings.isZoomControlsEnabled = true
            setOnMapClickListener { presenter.onMapClick(it) }
        }
    }

    override fun setEnableNextStepButton(i: Boolean) {
        nextStepButton.isEnabled = i
    }

    override fun addMarkerInGoogleMap(latLng: LatLng) {
        map?.apply {
            clear()
            addMarker(MarkerOptions().position(latLng))
        }
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    override fun setLocationPoliceReport(location: LatLng) {
        locationPoliceReport = location
    }

    override fun setTextPlaceAutocomplete(address: String) {
        placeAutocompleteFragment?.setText(address)
        this.address = address
    }

    override fun navigationToPoliceReportActivity() {
        startActivity(Intent(this, PoliceReportActivity::class.java)
                .putExtra("type_report", getTypePoliceReport())
                .putExtra("latitude", locationPoliceReport?.latitude)
                .putExtra("longitude", locationPoliceReport?.longitude)
                .putExtra("address", address))
    }

    override fun reverseGeocodingMessageError() {
        alert {
            messageResource = R.string.text_reverse_geocode
            titleResource = R.string.title_attention
        }.show()
    }

    fun getTypePoliceReport(): String = intent.extras.getString("type_report")


    fun setTitleToolbar(typeReport: String){
        when(typeReport) {
            PoliceReport.TYPE_POLICE_REPORT_AFFAIR -> {
                toolbar.apply {
                    setTitle(R.string.optional)
                    setSubtitle(R.string.incident_location)
                }
                setEnableNextStepButton(true)
            }

            PoliceReport.TYPE_POLICE_REPORT_AIRCRAFT-> {
                toolbar.apply {
                    setTitle(R.string.required)
                    setSubtitle(R.string.description_aircraft_map)
                }
            }

            PoliceReport.TYPE_POLICE_REPORT_DRUGS -> {
                toolbar.apply {
                    setTitle(R.string.required)
                    setSubtitle(R.string.selected_map_drug)
                }
            }

            PoliceReport.TYPE_POLICE_REPORT_ONLINE -> {
                toolbar.apply {
                    setTitle(R.string.required)
                    setSubtitle(R.string.incident_location)
                }
            }

            PoliceReport.TYPE_OTHER_POLICE_REPORT -> {
                toolbar.apply {
                    setTitle(R.string.required)
                    setSubtitle(R.string.incident_location)
                }
            }
        }

        toolbar.apply {
            navigationIcon = getDrawable(R.drawable.ic_arrow_back_white_24dp)
            setNavigationOnClickListener { onBackPressed() }
        }
    }
}
