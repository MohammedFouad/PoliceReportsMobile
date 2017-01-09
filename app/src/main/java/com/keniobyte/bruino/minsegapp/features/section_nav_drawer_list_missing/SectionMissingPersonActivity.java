package com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_missing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_missing.adapter.MissingPersonAdapterRecycler;
import com.keniobyte.bruino.minsegapp.model.Person;
import com.keniobyte.bruino.minsegapp.ui.PersonProfileActivity;
import com.keniobyte.bruino.minsegapp.utils.RecyclerItemClickListener;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(SectionMissingPersonPresenter.class)
public class SectionMissingPersonActivity extends AppCompatActivity implements ISectionMissingPersonView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.downloadPersonProgressBar) ProgressBar downloadMissingPersonProgressBar;
    @BindView(R.id.wantedRecyclerView) RecyclerView wantedRecyclerView;

    @BindString(R.string.section_missing) String titleActivity;
    @BindString(R.string.title_error) String titleError;
    @BindString(R.string.message_error_current_version) String messageConnectionError;
    @BindString(R.string.accept) String ok;

    @BindDrawable(R.drawable.ic_arrow_back_white_24dp) Drawable arrowBack;

    private Context context = this;
    private SectionMissingPersonPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_person_missing);
        ButterKnife.bind(this);

        toolbar.setTitle(titleActivity);
        toolbar.setNavigationIcon(arrowBack);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        wantedRecyclerView.setHasFixedSize(true);
        wantedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        wantedRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, wantedRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                presenter.itemMissingPerson(((MissingPersonAdapterRecycler) wantedRecyclerView.getAdapter()).getItem(position));
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));

        presenter = new SectionMissingPersonPresenter(context);
        presenter.addView(this);
        presenter.setPersonsInList();
    }

    @Override
    public MissingPersonAdapterRecycler createRecyclerViewAdapter(List<Person> persons) {
        return new MissingPersonAdapterRecycler(persons);
    }

    @Override
    public void setMissingRecyclerViewAdapter(MissingPersonAdapterRecycler adapter){
        wantedRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showProgress() {
        downloadMissingPersonProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        downloadMissingPersonProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void connectionErrorMessage() {
        new AlertDialog.Builder(context)
                .setTitle(titleError)
                .setMessage(messageConnectionError)
                .setCancelable(false)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void navigationToMissingPerson(Person person) {
        startActivity(new Intent(context, PersonProfileActivity.class)
                .putExtra("id", person.getId())
                .putExtra("name", person.getFullName())
                .putExtra("age", person.getAge())
                .putExtra("lastTimeSeen", person.getLastTimeSee())
                .putExtra("imageURL", person.getUrlProfile()));
    }
}
