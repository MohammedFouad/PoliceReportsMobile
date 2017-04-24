package com.keniobyte.bruino.minsegapp.features.section_police_stations;

import com.keniobyte.bruino.minsegapp.models.PoliceStation;

import org.json.JSONException;

import java.io.IOException;

/**
 * @author bruino
 * @version 16/01/17.
 */

public interface IPoliceStationsPresenter {
    void showPoliceStation() throws JSONException, IOException;
    void onClickMyPoliceStations();
    void onClickListPoliceStations();

    PoliceStation getPoliceStationById(Integer integer);
}
