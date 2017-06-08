package com.keniobyte.bruino.minsegapp.features.location_police_report

import android.support.design.widget.TabLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import com.google.android.gms.maps.model.LatLng
import com.keniobyte.bruino.minsegapp.R

class LocationPoliceReportActivity2 : AppCompatActivity(), MapFragment.OnPositionSelectedListener {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var mViewPager: ViewPager? = null
    private var latlngPoliceReport: LatLng? = LatLng(-26.8312148,-65.2033459)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_police_report2)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter

        val tabLayout = findViewById(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onPositionSelected(latLng: LatLng) {
        latlngPoliceReport = latLng
        mSectionsPagerAdapter?.update(latLng)
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        var positionPoliceReport: LatLng = LatLng(-26.8312148,-65.2033459)

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

        override fun getItemPosition(`object`: Any?): Int {
            if (`object` is IUpdateableStreetView) `object`.update(positionPoliceReport)
            return super.getItemPosition(`object`)
        }
    }
}
