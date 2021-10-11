package com.webexpertbd.docdirectory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.webexpertbd.docdirectory.adapter.DoctorListAdapter;
import com.webexpertbd.docdirectory.model.DoctorsModel;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addDoctor;
    FirebaseAuth mAuth;
    Toolbar toolbar;
    SearchView searchDoctorLocation;
    RecyclerView recyclerView;
    DatabaseReference database;
    DoctorListAdapter doctorListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> super.onBackPressed());
        addDoctor = findViewById(R.id.floatingActionButton);
        searchDoctorLocation = findViewById(R.id.searchDoctorLocation);
        recyclerView = findViewById(R.id.recyclerView);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Doctors");

        searchDoctorLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                seachPasswords(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                seachPasswords(query);
                return false;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<DoctorsModel> doctorList = new FirebaseRecyclerOptions.Builder<DoctorsModel>().setQuery(database,DoctorsModel.class).build();
        doctorListAdapter = new DoctorListAdapter(doctorList);
        recyclerView.setAdapter(doctorListAdapter);



        String currentUser = mAuth.getCurrentUser().getPhoneNumber();
        assert currentUser != null;
        if (currentUser.equals(getResources().getString(R.string.admin))){
            addDoctor.setVisibility(View.VISIBLE);
        }

        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddDoctorActivity.class));
            }
        });

    }

    private void seachPasswords(String query) {
        Query database = FirebaseDatabase.getInstance().getReference("Doctors").orderByChild("name").startAt(query).endAt(query.toUpperCase()+"\uf8ff");
        FirebaseRecyclerOptions<DoctorsModel> options
                = new FirebaseRecyclerOptions.Builder<DoctorsModel>()
                .setQuery(database, DoctorsModel.class)
                .build();
        doctorListAdapter = new DoctorListAdapter(options);
        doctorListAdapter.startListening();
        recyclerView.setAdapter(doctorListAdapter);
    }

    @Override protected void onStart()
    {
        super.onStart();
        doctorListAdapter.startListening();
        doctorListAdapter.notifyDataSetChanged();
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    1);
        }
    }

    @Override protected void onStop()
    {
        super.onStop();
        doctorListAdapter.stopListening();
    }
}