package com.supportivehands.salonmanagementapp.registeration.customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.supportivehands.salonmanagementapp.R;
import com.supportivehands.salonmanagementapp.customer.CustmerDashBoard;
import com.supportivehands.salonmanagementapp.databinding.ActivityLoginCustmerBinding;


import static com.supportivehands.salonmanagementapp.common.Common.goToo;

public class LoginCustmer extends AppCompatActivity {
    ActivityLoginCustmerBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_custmer);
        binding= ActivityLoginCustmerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();  //Getting instances of FirebaseAuth

        binding.registenow.setOnClickListener(view -> goToo(
                LoginCustmer.this, RegisterCustmer.class));


        binding.login.setOnClickListener(view -> {

            String Email = binding.email.getText().toString();
            final String pass = binding.paswrd.getText().toString();

            //Validation section
            if (TextUtils.isEmpty(Email)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }
            binding.progressBar.setVisibility(View.VISIBLE);
            if (pass.length() < 6) {
                binding.paswrd.setError("Should be greater than 6");
            }
            //authenticate user with email/password by adding complete listener
            auth.signInWithEmailAndPassword(Email, pass)
                    .addOnCompleteListener(LoginCustmer.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                // there was an error
                                Toast.makeText(LoginCustmer.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_LONG).show();
                                Log.e("MyTag", task.getException().toString());

                            } else {
                                Intent intent = new Intent(LoginCustmer.this, CustmerDashBoard.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

        });
    }

    //Checking current user is logging or not
    @Override
    public void onStart() {
        super.onStart();
        // If user logged in, go to sign-in screen
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, CustmerDashBoard.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.progressBar.setVisibility(View.GONE);
    }
}