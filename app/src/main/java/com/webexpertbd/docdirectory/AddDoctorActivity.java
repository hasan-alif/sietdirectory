package com.webexpertbd.docdirectory;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.webexpertbd.docdirectory.model.DoctorsModel;

import org.jetbrains.annotations.NotNull;

public class AddDoctorActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView doctorProfilePic;
    ImageButton uploadDoctorProfile;
    ProgressBar doctorAddProgressbar;
    EditText doctorName,doctorHospital,doctorPhoneNumber,doctorDegrees,doctorExperience;
    Button saveDoctorInformation;
    Spinner doctorDepartment;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    DatabaseReference database;
    String profilePictureUrl, department;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);
        toolbar = findViewById(R.id.toolbarAddDoctor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> super.onBackPressed());
        doctorProfilePic = findViewById(R.id.doctorProfilePic);
        uploadDoctorProfile = findViewById(R.id.uploadDoctorProfile);
        doctorAddProgressbar = findViewById(R.id.doctorAddProgressbar);
        doctorName = findViewById(R.id.doctorName);
        doctorHospital = findViewById(R.id.doctorHospital);
        doctorPhoneNumber = findViewById(R.id.doctorPhoneNumber);
        doctorDegrees = findViewById(R.id.doctorDegrees);
        doctorExperience = findViewById(R.id.doctorExperience);
        saveDoctorInformation = findViewById(R.id.saveDoctorInformation);
        doctorDepartment = findViewById(R.id.doctorDepartment);
        //Firebase Initialization
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance().getReference("Doctors");

        saveDoctorInformation.setOnClickListener(v -> saveToFirebase());

        uploadDoctorProfile.setOnClickListener(v -> mGetContent.launch("image/*"));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.department, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorDepartment.setAdapter(adapter);
        doctorDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department = doctorDepartment.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                department = "Surgeon";
            }
        });
    }

    private void saveToFirebase() {
        doctorAddProgressbar.setVisibility(View.VISIBLE);
        String name = doctorName.getText().toString().trim();
        String hospital = doctorHospital.getText().toString().trim();
        String number = doctorPhoneNumber.getText().toString().trim();
        String degree = doctorDegrees.getText().toString().trim();
        String experience = doctorExperience.getText().toString().trim();
        String key = database.child(mAuth.getUid()).push().getKey();
        DoctorsModel doctorsModel = new DoctorsModel(name,department,hospital,number,degree,experience,profilePictureUrl,key);
        database.child(key).setValue(doctorsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()){
                    doctorAddProgressbar.setVisibility(View.GONE);
                    Toast.makeText(AddDoctorActivity.this, "Doctor added to the database", Toast.LENGTH_SHORT).show();
                    AddDoctorActivity.super.onBackPressed();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(AddDoctorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    uploadDoctorProfile.setVisibility(View.GONE);
                    doctorProfilePic.setVisibility(View.VISIBLE);
                    doctorProfilePic.setImageURI(uri);
                    doctorAddProgressbar.setVisibility(View.VISIBLE);
                    uploadPicture(uri);
                }
            });
    private void uploadPicture(Uri uri) {
        StorageReference profilePicture = storageReference.child("Doctors/" + System.currentTimeMillis());
        profilePicture.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            profilePicture.getDownloadUrl().addOnSuccessListener(uri1 -> profilePictureUrl = uri1.toString());
            doctorAddProgressbar.setVisibility(View.GONE);
            Toast.makeText(this, "Upload Successful", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show());

    }
}