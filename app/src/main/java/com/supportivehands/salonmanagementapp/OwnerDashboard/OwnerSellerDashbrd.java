package com.supportivehands.salonmanagementapp.OwnerDashboard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.supportivehands.salonmanagementapp.R;
import com.supportivehands.salonmanagementapp.activitties.ProfileActivity;
import com.supportivehands.salonmanagementapp.customer.CustmerDashBoard;
import com.supportivehands.salonmanagementapp.databinding.ActivityOwnerDashbrdBinding;
import com.supportivehands.salonmanagementapp.model.ModelAddService;
import com.supportivehands.salonmanagementapp.registeration.SelectionMode;
import com.supportivehands.salonmanagementapp.shopservices.ShopServices;

import java.util.ArrayList;
import java.util.Objects;

public class OwnerSellerDashbrd extends AppCompatActivity  {


    private ActivityOwnerDashbrdBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String stauts;
    private static ProgressDialog progressDialog;

    AdapterServices adapter;
    ArrayList<ModelAddService> list = new ArrayList<>();
    DatabaseReference dbRef;
    public static String lat,lng,mobileSeller;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityOwnerDashbrdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Get a reference to our posts
        mAuth=FirebaseAuth.getInstance();
        dbRef=FirebaseDatabase.getInstance().getReference("Services");
        mUser=mAuth.getCurrentUser();
        mAuth=FirebaseAuth.getInstance();
        uid= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        //show user profile
       getUserProfilePic();
       //getMyServices
        getMyService();
//click on profile pic
        binding.idProfile.setOnClickListener(view -> {
            startActivity(new Intent(OwnerSellerDashbrd.this, ProfileActivity.class));

        });
        //add service button click or floating button
        binding.idaddService.setOnClickListener(view -> {
            if (stauts.equals("Pending")){
                Toast.makeText(this, "You need Admin Approvel, wait for approvel", Toast.LENGTH_SHORT).show();
            }else if (stauts.equals("Rejected")){
                Toast.makeText(this, "Sorry your profile is Rejected", Toast.LENGTH_SHORT).show();
            }else if (stauts.equals("Approved")){
                startActivity(new Intent(OwnerSellerDashbrd.this, ShopServices.class));

            }
        });
        binding.idorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stauts.equals("Pending")){
                    Toast.makeText(OwnerSellerDashbrd.this, "You need Admin Approvel, wait for approvel", Toast.LENGTH_SHORT).show();
                }else if (stauts.equals("Rejected")){
                    Toast.makeText(OwnerSellerDashbrd.this, "Sorry your profile is Rejected", Toast.LENGTH_SHORT).show();
                }else if (stauts.equals("Approved")){
                    startActivity(new Intent(OwnerSellerDashbrd.this, OrderRecieved.class));

                }
            }
        });

    }

    //get service that i added in my shop
    private void getMyService() {

        showLoadingDialog(OwnerSellerDashbrd.this,"onCreatePendingShopAdmin");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot:snapshot.getChildren()){
                    ModelAddService modelAddService=datasnapshot.getValue(ModelAddService.class);

                  if (modelAddService.getUid().equals(uid)){
                        list.add(modelAddService);

                    }
                    hideDialog();

                }
                hideDialog();
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OwnerSellerDashbrd.this, "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();

            }
        });
       binding.recylerview.setHasFixedSize(true);
        binding.recylerview.setLayoutManager(new LinearLayoutManager(this));
        adapter=new AdapterServices(OwnerSellerDashbrd.this,list);
        binding.recylerview.setAdapter(adapter);

    }

    //user profile pic show
    private void getUserProfilePic() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");

        ref.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String url = snapshot.child("lgo").getValue().toString();
                    ImageView imgdp = findViewById(R.id.id_profile);
                    stauts=snapshot.child("status").getValue().toString();
                    Glide.with(getApplicationContext()).load(url).into(imgdp);
                    lat=snapshot.child("lat").getValue().toString();
                    lng=snapshot.child("lng").getValue().toString();
                     mobileSeller=snapshot.child("mobileNo").getValue().toString();

                }else {
                    Toast.makeText(OwnerSellerDashbrd.this, "Sorry, Your Email Account type is Customer", Toast.LENGTH_LONG).show();
                    new Thread( new Runnable() { @Override public void run() {
                        // Run whatever background code you want here.

                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(OwnerSellerDashbrd.this, SelectionMode.class));
                        finish();

                    } } ).start();
                    hideDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideDialog();
            }
        });
    }
    public static ProgressDialog showLoadingDialog(Activity activity, String callingPlace) {

        progressDialog = new ProgressDialog(activity);

        if (!progressDialog.isShowing()) {
            progressDialog.show();



        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
//        Sprite doubleBounce = new DoubleBounce();
//        progressDialog.setIndeterminateDrawable(doubleBounce);
        progressDialog.setCanceledOnTouchOutside(true);
        }
        return progressDialog;

    }
    public static void hideDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {

            progressDialog.hide();
        }
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
        super.onBackPressed();
    }
}