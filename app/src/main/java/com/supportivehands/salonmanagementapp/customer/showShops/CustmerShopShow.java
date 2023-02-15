package com.supportivehands.salonmanagementapp.customer.showShops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.supportivehands.salonmanagementapp.R;
import com.supportivehands.salonmanagementapp.admin.ModelPendingShop.UserProvider;
import com.supportivehands.salonmanagementapp.databinding.ActivityCustmerShopShowBinding;

import java.util.ArrayList;

import static com.supportivehands.salonmanagementapp.OwnerDashboard.OwnerSellerDashbrd.hideDialog;
import static com.supportivehands.salonmanagementapp.OwnerDashboard.OwnerSellerDashbrd.showLoadingDialog;
import static com.supportivehands.salonmanagementapp.customer.CustmerDashBoard.city;


public class CustmerShopShow extends AppCompatActivity {
    ActivityCustmerShopShowBinding binding;
    RecyclerView recyclerView;
    AdptrShowShop adapter;
    ArrayList<UserProvider> list = new ArrayList<>();
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCustmerShopShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toast.makeText(this, ""+city, Toast.LENGTH_SHORT).show();
        recyclerView=findViewById(R.id.shoprecylerview);
        dbRef= FirebaseDatabase.getInstance().getReference("Users");



        showLoadingDialog(CustmerShopShow.this,"onCreatePendingShopAdmin");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot:snapshot.getChildren()){
                    UserProvider userProvider=datasnapshot.getValue(UserProvider.class);
                    if (userProvider.getStatus().equals("Approved") && userProvider.getCity().equals(city)){
                        list.add(userProvider);
                    }
                    hideDialog();
                    adapter.notifyDataSetChanged();

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustmerShopShow.this, "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();

            }
        });
//recylerview data into adapter class
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        list=new ArrayList<>();
        adapter=new AdptrShowShop(CustmerShopShow.this,list,"Approved");
        recyclerView.setAdapter(adapter);
    }

}