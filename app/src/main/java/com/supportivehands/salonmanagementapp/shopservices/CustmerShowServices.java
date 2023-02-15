package com.supportivehands.salonmanagementapp.shopservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.supportivehands.salonmanagementapp.databinding.ActivityCustmerShowServicesBinding;
import com.supportivehands.salonmanagementapp.model.ModelAddService;

import java.util.ArrayList;

import static com.supportivehands.salonmanagementapp.OwnerDashboard.OwnerSellerDashbrd.hideDialog;
import static com.supportivehands.salonmanagementapp.OwnerDashboard.OwnerSellerDashbrd.showLoadingDialog;

public class CustmerShowServices extends AppCompatActivity {
    ActivityCustmerShowServicesBinding binding;
    DatabaseReference dbRef;
    AdapterServicesShow adapter;
    ArrayList<ModelAddService> list = new ArrayList<>();
    public static String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCustmerShowServicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbRef= FirebaseDatabase.getInstance().getReference("Services");
        getMyService();
    }
    //get service that i added in my shop
    private void getMyService() {

        showLoadingDialog(CustmerShowServices.this,"onCreatePendingShopAdmin");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot:snapshot.getChildren()){
                    ModelAddService modelAddService=datasnapshot.getValue(ModelAddService.class);

                    if (modelAddService.getUid().equals(uid) && modelAddService.getStatus().equals("Yes")){

                        list.add(modelAddService);

                    }
                    hideDialog();

                }
                hideDialog();
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustmerShowServices.this, "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();

            }
        });
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String buyerUid=currentFirebaseUser.getUid();
        binding.recylerview.setHasFixedSize(true);
        binding.recylerview.setLayoutManager(new LinearLayoutManager(this));
        adapter=new AdapterServicesShow(CustmerShowServices.this,list,buyerUid);
        binding.recylerview.setAdapter(adapter);

    }
}