package com.supportivehands.salonmanagementapp.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.supportivehands.salonmanagementapp.R;
import com.supportivehands.salonmanagementapp.databinding.ActivityAdminLoginBinding;
import com.supportivehands.salonmanagementapp.databinding.ActivityLoginBinding;

public class AdminLogin extends AppCompatActivity {
    ActivityAdminLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAdminLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.loginadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=binding.email.getText().toString();
                String pass=binding.paswrd.getText().toString();

                if (email.equals("admin@gmail.com") && pass.equals("admin123")){
                    startActivity(new Intent(AdminLogin.this,AdminDashbrd.class));
                    finish();
                }else {
                    Toast.makeText(AdminLogin.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}