package com.keniobyte.bruino.minsegapp.utils

import android.app.Activity
import android.content.Intent

/**
 * @author bruino
 * @version 22/05/17.
 */

inline fun <reified T: Activity> Activity.startActivity() {
    startActivity(Intent(this, T::class.java))
}