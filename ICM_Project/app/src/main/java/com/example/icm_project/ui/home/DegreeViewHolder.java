package com.example.icm_project.ui.home;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icm_project.R;

public class DegreeViewHolder extends RecyclerView.ViewHolder {

    private TextView view;
    public DegreeViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.semester);
    }

    public TextView getView(){
        return view;
    }

}
