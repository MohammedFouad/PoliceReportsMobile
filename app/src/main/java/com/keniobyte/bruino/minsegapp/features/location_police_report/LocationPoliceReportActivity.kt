package com.keniobyte.bruino.minsegapp.features.location_police_report

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.google.android.gms.maps.model.LatLng
import com.keniobyte.bruino.minsegapp.R
import com.keniobyte.bruino.minsegapp.features.police_report.PoliceReportActivity
import com.keniobyte.bruino.minsegapp.models.PoliceReport
import kotlinx.android.synthetic.main.activity_location_police_report2.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class LocationPoliceReportActivity : AppCompatActivity(), ILocationPoliceReportView
        , MapFragment.OnPositionSelectedListener, StreetViewFragment.OnChangedPositionStreetView{
    private val presenter: ILocationPoliceReportPresenter<ILocationPoliceReportView>? by lazy { LocationPoliceReportPresenter(this) }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var mViewPager: ViewPager? = null
    private var mLatLngPoliceReport: LatLng? = null
    private var mBearingPoliceReport: Float = 0F
    private var mTiltPoliceReport: Float = 0F

    private var mAddressPoliceReport: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_police_report2)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        mViewPager = findViewById(R.id.container) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter

        val tabLayout = findViewById(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)

        next.visibility = View.INVISIBLE
        next.setOnClickListener { presenter?.nextStep() }

        setTitleToolbar(getTypePoliceReport())
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.unregisterReceiver()
    }

    fun getTypePoliceReport(): String = intent.extras.getString("type_report")

    override fun setLocationPoliceReport(location: LatLng) {
        onPositionSelected(location)
    }

    override fun setEnableNextStepButton(i: Boolean) {
        next.visibility = if (i) View.VISIBLE else View.INVISIBLE
    }

    override fun setTextPlaceAutocomplete(address: String) {
        mAddressPoliceReport = address
        mSectionsPagerAdapter?.updateAddress(address)
    }

    override fun navigationToPoliceReportActivity() {
        startActivity(Intent(this, PoliceReportActivity::class.java)
                .putExtra("type_report", getTypePoliceReport())
                .putExtra("latitude", mLatLngPoliceReport?.latitude)
                .putExtra("longitude", mLatLngPoliceReport?.longitude)
                .putExtra("address", mAddressPoliceReport)
                .putExtra("bearing", mBearingPoliceReport)
                .putExtra("tilt", mTiltPoliceReport))
    }

    override fun reverseGeocodingMessageError() {
        alert {
            setTitle(R.string.title_attention)
            messageResource = R.string.text_reverse_geocode
            yesButton { this@LocationPoliceReportActivity.toast(R.string.title_error) }
        }
    }

    override fun onPositionSelected(latLng: LatLng) {
        mLatLngPoliceReport = latLng
        presenter?.geocodingReverse(latLng)
        mSectionsPagerAdapter?.update(latLng)

        //Reset bearing and tilt camera
        mBearingPoliceReport = 0F
        mTiltPoliceReport = 0F
    }

    override fun onChangedLatLngStreetView(latLng: LatLng) {
        // Change the location move in tab streetview, keep the angles camera
        mLatLngPoliceReport = latLng
        mSectionsPagerAdapter?.update(latLng)
        setEnableNextStepButton(true)
    }

    override fun onChangedCameraStreetView(bearing: Float, tilt: Float) {
        mBearingPoliceReport = bearing
        mTiltPoliceReport = tilt
    }

    fun setTitleToolbar(typeReport: String){
        when(typeReport) {
            PoliceReport.TYPE_POLICE_REPORT_AFFAIR -> {
                supportActionBar?.apply {
                    setTitle(R.string.optional)
                    setSubtitle(R.string.incident_location)
                }
                setEnableNextStepButton(true)
            }

            PoliceReport.TYPE_POLICE_REPORT_AIRCRAFT -> {
                supportActionBar?.apply {
                    setTitle(R.string.required)
                    setSubtitle(R.string.description_aircraft_map)
                }
            }

            PoliceReport.TYPE_POLICE_REPORT_DRUGS -> {
                supportActionBar?.apply {
                    setTitle(R.string.required)
                    setSubtitle(R.string.selected_map_drug)
                }
            }

            PoliceReport.TYPE_POLICE_REPORT_ONLINE -> {
                supportActionBar?.apply {
                    setTitle(R.string.required)
                    setSubtitle(R.string.incident_location)
                }
            }

            PoliceReport.TYPE_OTHER_POLICE_REPORT -> {
                supportActionBar?.apply {
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

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        var positionPoliceReport: LatLng = LatLng(-26.8312148,-65.2033459)
        var address: String? = null

        override fun getItem(position: Int): Fragment? {
            when (position) {
                0    -> return MapFragment()
                1    -> return StreetViewFragment().newInstance(positionPoliceReport.latitude, positionPoliceReport.longitude)
                else -> return null
            }
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return getString(R.string.map)
                1 -> return getString(R.string.street_view)
            }
            return null
        }

        fun update(data: LatLng) {
            this.positionPoliceReport = data
            notifyDataSetChanged()
        }

        fun updateAddress(address: String?) {
            this.address = address
        }

        override fun getItemPosition(`object`: Any?): Int {
            if (`object` is IUpdateableStreetView) `object`.update(positionPoliceReport)
            if (`object` is IUpdateAddressPlaceAutocomplete) `object`.update(address)
            if (`object` is IUpdateLocationMapFragment) `object`.update(positionPoliceReport)
            return super.getItemPosition(`object`)
        }
    }
}
