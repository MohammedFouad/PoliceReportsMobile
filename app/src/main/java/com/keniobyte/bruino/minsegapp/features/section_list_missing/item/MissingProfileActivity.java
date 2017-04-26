package com.keniobyte.bruino.minsegapp.features.section_list_missing.item;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.features.section_list_missing.missing_report.MissingReportActivity;
import com.keniobyte.bruino.minsegapp.utils.SquareImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(MissingProfilePresenter.class)
public class MissingProfileActivity extends AppCompatActivity implements IMissingProfileView {

    @BindView(R.id.myToolbar) Toolbar toolbar;
    @BindView(R.id.missingProfileImageView) SquareImageView missingProfileImageView;
    @BindView(R.id.loadImageProgressBar) ProgressBar loadImageProgressBar;
    @BindView(R.id.missingNameTextView) TextView missingNameTextView;
    @BindView(R.id.missingAgeTextView) TextView missingAgeTextView;

    @BindString(R.string.section_missing) String titleToolbar;
    @BindString(R.string.error_share) String sharedMessageError;
    @BindDrawable(R.drawable.ic_arrow_back_white_24dp) Drawable arrowBack;
    @BindDrawable(R.drawable.ic_error_black_24dp) Drawable error;

    private Context context = this;
    private MissingProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missing_activity_profile);
        ButterKnife.bind(this);

        toolbar.setTitle(titleToolbar);
        toolbar.setNavigationIcon(arrowBack);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setSupportActionBar(toolbar);

        presenter = new MissingProfilePresenter(context);
        presenter.addView(this);
        presenter.displayMissingPerson();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_missing_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                onClickShare();
                break;
            case R.id.action_provide_data:
                onClickSendMissingReport();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getId() {
        return getIntent().getExtras().getInt("id");
    }

    @Override
    public String getUrlProfile() {
        return getIntent().getExtras().getString("imageURL");
    }

    @Override
    public String getMissingName() {
        return getIntent().getExtras().getString("name");
    }


    @Override
    public int getMissingAge() {
        return getIntent().getExtras().getInt("age");
    }

    @Override
    public ImageView getMissingProfileImage() {
        return missingProfileImageView;
    }

    @Override
    public void setMissingProfileImage(String url) {
        Picasso.with(context).load(url).error(error).into(missingProfileImageView, new Callback() {
            @Override
            public void onSuccess() {
                hideProgress();
            }

            @Override
            public void onError() {
                hideProgress();
            }
        });
    }

    @Override
    public void setMissingName(String name) {
        missingNameTextView.setText(name);
    }

    @Override
    public void setMissingAge(String age) {
        missingAgeTextView.setText(age);
    }

    @Override
    public void showProgress() {
        loadImageProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        loadImageProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClickShare() {
        presenter.shareMissing();
    }

    @Override
    public void onClickSendMissingReport() {
        presenter.createMissingReport();
    }

    @Override
    public void navigationToMissingReport() {
        startActivity(new Intent(context, MissingReportActivity.class)
                .putExtra("id", getId()));
    }

    @Override
    public void sharedMissingPerson(String body, Uri uriImage) {
        startActivity(Intent.createChooser(new Intent()
                .setAction(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, body)
                .putExtra(Intent.EXTRA_STREAM, uriImage)
                .setType("image/*"), "Share Missing"));
    }

    @Override
    public void sharedMessageError() {
        Toast.makeText(context, sharedMessageError, Toast.LENGTH_SHORT).show();
    }
}
