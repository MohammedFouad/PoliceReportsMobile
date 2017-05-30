package com.keniobyte.bruino.minsegapp
import android.app.Application

/**
 * @author bruino
 * @version 04/05/17.
 */

class App : Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}