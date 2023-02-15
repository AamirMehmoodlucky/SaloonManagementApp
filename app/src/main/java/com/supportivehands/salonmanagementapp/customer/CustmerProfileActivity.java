package com.supportivehands.salonmanagementapp.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.supportivehands.salonmanagementapp.R;
import com.supportivehands.salonmanagementapp.activitties.ProfileActivity;
import com.supportivehands.salonmanagementapp.databinding.ActivityCustmerProfileBinding;
import com.supportivehands.salonmanagementapp.registeration.LoginActivity;
import com.supportivehands.salonmanagementapp.registeration.SelectionMode;
import com.supportivehands.salonmanagementapp.registeration.customers.LoginCustmer;

public class CustmerProfileActivity extends AppCompatActivity {
    ActivityCustmerProfileBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCustmerProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Get a reference to our posts
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        //set profile data in textview and profile pic
        getUserProfilePic();

        //logOut textview click for logout from app
        binding.idlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                finishAffinity();

            }
        });
    }
    //signout function
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, SelectionMode.class));
        finish();
    }
    //set profile data..call in onCreate
    private void getUserProfilePic() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Custmers");

        ref.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue().toString();
                    String mobileno = snapshot.child("mobie").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();
                    String url = snapshot.child("propfilepic").getValue().toString();



                    binding.idprofilename.setText(name);
                    binding.idmobileNO.setText(mobileno);
                    binding.idemailp.setText(email);


                    Glide.with(CustmerProfileActivity.this).load(url).into(binding.iddp);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}