package com.supportivehands.salonmanagementapp.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.supportivehands.salonmanagementapp.customer.showShops.CustmerShopShow;
import com.supportivehands.salonmanagementapp.databinding.ActivityCustmerDashBoardBinding;
import com.supportivehands.salonmanagementapp.orders.MyOrdersList;
import com.supportivehands.salonmanagementapp.registeration.SelectionMode;

import java.util.ArrayList;
import java.util.Objects;

import static com.supportivehands.salonmanagementapp.OwnerDashboard.OwnerSellerDashbrd.hideDialog;
import static com.supportivehands.salonmanagementapp.OwnerDashboard.OwnerSellerDashbrd.showLoadingDialog;

public class CustmerDashBoard extends AppCompatActivity {
    ActivityCustmerDashBoardBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String uid;
  public static String city,mobileBuyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCustmerDashBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mAuth=FirebaseAuth.getInstance();
        uid= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        //show user profile
        showLoadingDialog(CustmerDashBoard.this,"onCusterDashboard");
        getUserProfilePic();
//go to my profile (data and logout etc)
        binding.idProfile.setOnClickListener(view -> {
            startActivity(new Intent(CustmerDashBoard.this, CustmerProfileActivity.class));

        });
        //adding city in spiner
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select City");
        arrayList.add("Abbottabad");
        arrayList.add("Havelian");
        arrayList.add("Mansehra");
        arrayList.add("Huripur");
        arrayList.add("Peshawar");
        arrayList.add("Islamabad");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item
                , arrayList);
        binding.spinner1.setAdapter(arrayAdapter);
        binding.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 city = parent.getItemAtPosition(position).toString();
                ((TextView) view).setTextColor(getResources().getColor(R.color.golden));

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        //search button click for search on bases of selceted city
        binding.btnSearchShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (city.equals("Select City")){
                    Toast.makeText(CustmerDashBoard.this, "Please Select City", Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(new Intent(CustmerDashBoard.this, CustmerShopShow.class));
                }
            }
        });
        binding.idmyorderlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustmerDashBoard.this, MyOrdersList.class));
            }
        });
    }
    private void getUserProfilePic() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Custmers");

        ref.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String url = snapshot.child("propfilepic").getValue().toString();
                    ImageView imgdp = findViewById(R.id.id_profile);
                    Glide.with(CustmerDashBoard.this).load(url).into(imgdp);
                    mobileBuyer=snapshot.child("mobie").getValue().toString();
                    hideDialog();
//
//                ;

                }else {
                    Toast.makeText(CustmerDashBoard.this, "Sorry, Your Email Account type is Saloon Owner", Toast.LENGTH_LONG).show();
                    new Thread( new Runnable() { @Override public void run() {
                        // Run whatever background code you want here.

                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(CustmerDashBoard.this, SelectionMode.class));
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
}