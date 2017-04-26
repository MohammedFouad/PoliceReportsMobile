package com.keniobyte.bruino.minsegapp.features.section_list_wanted.item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.views.base.BasePresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author bruino
 * @version 26/04/17.
 */

public class WantedProfilePresenter extends BasePresenter<WantedProfileActivity> implements IWantedProfilePresenter {
    private Context context;
    private WantedProfileActivity wantedProfileView;

    public WantedProfilePresenter(Context context) {
        this.context = context;
    }

    public void addView(WantedProfileActivity missingProfileView){
        this.wantedProfileView = missingProfileView;
    }

    @Override
    public void displayWantedPerson() {
        wantedProfileView.showProgress();

        String url = wantedProfileView.getUrlProfile();
        String name = wantedProfileView.getWantedName();
        String crime = wantedProfileView.getWantedCrime();
        String age = String.valueOf(wantedProfileView.getWantedAge()) + " " + context.getResources().getString(R.string.year);

        wantedProfileView.setWantedProfileImage(url);
        wantedProfileView.setWantedName(name);
        wantedProfileView.setWantedCrime(crime);
        wantedProfileView.setWantedAge(age);
    }

    @Override
    public void createWantedReport() {
        wantedProfileView.navigationToWantedReport();
    }

    @Override
    public void shareWanted() {
        Uri uri = getLocalBitmapUri(wantedProfileView.getWantedProfileImage());
        if (uri != null){
            String body = new StringBuilder().append(context.getResources().getString(R.string.app_name))
                    .append(": ").append(context.getResources().getString(R.string.section_wanted))
                    .append("\n" + context.getResources().getString(R.string.name) + ": ")
                    .append(wantedProfileView.getWantedName())
                    .append("\n" + context.getResources().getString(R.string.crime) + ": ")
                    .append(wantedProfileView.getWantedCrime())
                    .append("\n" + context.getResources().getString(R.string.age) + ": ")
                    .append(wantedProfileView.getWantedAge())
                    .append("\n" + context.getResources().getString(R.string.section_provideData))
                    .append(": ").append(context.getResources().getString(R.string.call_911))
                    .toString();
            wantedProfileView.sharedWantedPerson(body, uri);
        } else {
            wantedProfileView.sharedMessageError();
        }

    }

    //@link http://stackoverflow.com/questions/29638747/share-image-with-shareactionprovider-from-picasso?rq=1
    private Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "person_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
