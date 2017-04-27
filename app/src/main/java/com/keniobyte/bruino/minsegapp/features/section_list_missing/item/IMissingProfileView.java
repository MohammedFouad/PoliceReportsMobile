package com.keniobyte.bruino.minsegapp.features.section_list_missing.item;

import android.net.Uri;
import android.widget.ImageView;

/**
 * @author bruino
 * @version 09/01/17.
 */

public interface IMissingProfileView {
    int getId();
    String getUrlProfile();
    String getMissingName();
    int getMissingAge();
    ImageView getMissingProfileImage();

    void setMissingProfileImage(String url);
    void setMissingName(String name);
    void setMissingAge(String age);
    void showProgress();
    void hideProgress();
    void onClickShare();
    void onClickSendMissingReport();
    void navigationToMissingReport();
    void sharedMissingPerson(String body, Uri uriImage);
    void sharedMessageError();
}
