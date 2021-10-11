package com.webexpertbd.docdirectory.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.webexpertbd.docdirectory.R;
import com.webexpertbd.docdirectory.model.DoctorsModel;

public class DoctorListAdapter extends FirebaseRecyclerAdapter<
        DoctorsModel, DoctorListAdapter.ViewHolder> {


    public DoctorListAdapter(@NonNull FirebaseRecyclerOptions<DoctorsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull DoctorsModel model) {
        holder.name.setText(model.getName());
        holder.department.setText(model.getDepartment());
        holder.degree.setText(model.getDegree());
        holder.hospital.setText(model.getHospital());
        holder.experience.setText(model.getExperience() + " Years Of Experience");
        holder.booking.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+ model.getNumber()));//change the number
            v.getContext().startActivity(callIntent);
        });
        Glide.with(holder.view.getContext()).load(model.getPicture()).circleCrop().into(holder.image);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctors_view,parent,false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, department, experience, hospital, degree;
        AppCompatButton booking, favourite;
        ImageView image;
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.doctorNameView);
            department = itemView.findViewById(R.id.doctorDepartmentView);
            experience = itemView.findViewById(R.id.experienceTimeView);
            hospital = itemView.findViewById(R.id.doctorHospitalView);
            degree = itemView.findViewById(R.id.doctorDegreeView);
            booking = itemView.findViewById(R.id.doctorAppointmentView);
            favourite = itemView.findViewById(R.id.addToFavouriteView);
            image = itemView.findViewById(R.id.doctorImageView);
        }
    }
}
