package com.supportivehands.salonmanagementapp.OwnerDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.supportivehands.salonmanagementapp.R;
import com.supportivehands.salonmanagementapp.customer.AdapterOrder;

import com.supportivehands.salonmanagementapp.databinding.ActivityOrderRecievedBinding;
import com.supportivehands.salonmanagementapp.orders.ModelOrder;

import java.util.ArrayList;
import java.util.Objects;

import static com.supportivehands.salonmanagementapp.OwnerDashboard.OwnerSellerDashbrd.hideDialog;
import static com.supportivehands.salonmanagementapp.OwnerDashboard.OwnerSellerDashbrd.showLoadingDialog;

public class OrderRecieved extends AppCompatActivity {
    private ActivityOrderRecievedBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    AdapterOrder adapter;
    ArrayList<ModelOrder> list = new ArrayList<>();
    DatabaseReference dbRef;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderRecievedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get a reference to our posts
        mAuth=FirebaseAuth.getInstance();
        dbRef= FirebaseDatabase.getInstance().getReference("Orderslist");
        mUser=mAuth.getCurrentUser();
        mAuth=FirebaseAuth.getInstance();
        uid= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        //getMyServices
        getMyService();
    }
    //get service that i added in my shop
    private void getMyService() {

        showLoadingDialog(OrderRecieved.this,"onCreatePendingShopAdmin");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot:snapshot.getChildren()){
                    ModelOrder myOrders=datasnapshot.getValue(ModelOrder.class);

                    if (myOrders.getUidseller().equals(uid)){
                        list.add(myOrders);

                    }
                    hideDialog();

                }
                hideDialog();
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderRecieved.this, "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();

            }
        });
        binding.recylerview.setHasFixedSize(true);
        binding.recylerview.setLayoutManager(new LinearLayoutManager(this));
        adapter=new AdapterOrder(OrderRecieved.this,list,"2");
        binding.recylerview.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
       startActivity(new Intent(OrderRecieved.this,OwnerSellerDashbrd.class));
    }
}