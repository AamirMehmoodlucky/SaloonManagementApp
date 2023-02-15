package com.supportivehands.salonmanagementapp.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.supportivehands.salonmanagementapp.R;
import com.supportivehands.salonmanagementapp.databinding.ActivityAdminDashbrdBinding;

public class AdminDashbrd extends AppCompatActivity {
    ActivityAdminDashbrdBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAdminDashbrdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnPendingShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashbrd.this,AdminPendingShops.class));
            }
        });
        binding.idApprovedShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashbrd.this,AdminApprovedShop.class));
            }
        });
    }
}