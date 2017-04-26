package com.keniobyte.bruino.minsegapp.features.section_list_wanted.item;

import android.net.Uri;
import android.widget.ImageView;

/**
 * @author bruino
 * @version 26/04/17.
 */

public interface IWantedProfileView {
    int getId();
    String getUrlProfile();
    String getWantedName();;
    String getWantedCrime();
    int getWantedAge();
    ImageView getWantedProfileImage();

    void setWantedProfileImage(String url);
    void setWantedName(String name);
    void setWantedCrime(String crime);
    void setWantedAge(String age);
    void showProgress();
    void hideProgress();
    void onClickShare();
    void onClickSendWantedReport();
    void navigationToWantedReport();
    void sharedWantedPerson(String body, Uri uriImage);
    void sharedMessageError();
}
