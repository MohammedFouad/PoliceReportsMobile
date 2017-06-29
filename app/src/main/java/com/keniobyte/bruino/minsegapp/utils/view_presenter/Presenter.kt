package com.keniobyte.bruino.minsegapp.utils.view_presenter

/**
 * @author bruino
 * @version 19/05/17.
 */
interface Presenter<T: View> {
    var view: T?

    fun onDestroy(){
        view = null
    }
}