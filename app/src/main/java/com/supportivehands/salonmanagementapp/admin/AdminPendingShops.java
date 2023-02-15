package com.supportivehands.salonmanagementapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.supportivehands.salonmanagementapp.R;
import com.supportivehands.salonmanagementapp.admin.ModelPendingShop.ShopInterface;
import com.supportivehands.salonmanagementapp.admin.ModelPendingShop.UserProvider;
import com.supportivehands.salonmanagementapp.admin.adptrs.AdptrPendingShop;

import java.util.ArrayList;
import java.util.HashMap;

import static com.supportivehands.salonmanagementapp.OwnerDashboard.OwnerSellerDashbrd.hideDialog;
import static com.supportivehands.salonmanagementapp.OwnerDashboard.OwnerSellerDashbrd.showLoadingDialog;

public class AdminPendingShops extends AppCompatActivity implements ShopInterface {
    RecyclerView recyclerView;
    AdptrPendingShop adapter;
    ArrayList<UserProvider> list = new ArrayList<>();
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pending_shops);
        recyclerView=findViewById(R.id.shoprecylerview);
        dbRef=FirebaseDatabase.getInstance().getReference("Users");


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        adapter=new AdptrPendingShop(AdminPendingShops.this,list,"Pending",this);
        recyclerView.setAdapter(adapter);

        showLoadingDialog(AdminPendingShops.this,"onCreatePendingShopAdmin");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot:snapshot.getChildren()){
                    UserProvider userProvider=datasnapshot.getValue(UserProvider.class);

                   if (userProvider.getStatus().equals("Pending") ){
                        list.add(userProvider);

                 }
                    hideDialog();

                }
                adapter.notifyDataSetChanged();
                hideDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminPendingShops.this, "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();

            }
        });
    }

    @Override
    public void reomveAndUpdate(String UID, int pos, String statusNew, Context ctxt) {
        showAlertDiag( UID, pos, statusNew, ctxt);

    }
    private void showAlertDiag(String UID,int pos,String statusNew,Context ctxt) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(ctxt);
        if (statusNew.equals("Pending")){
            alertbox.setMessage("Are you sure you want to put this shop in Pending?");
        }else if (statusNew.equals("Approved")){
            alertbox.setMessage("Are you sure you want to approve this shop?");

        }

        alertbox.setTitle("Alert..!");
        alertbox.setIcon(R.drawable.logo);

        alertbox.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,
                                        int arg1) {

                        list.clear();
                        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap hashMap=new HashMap();
                        hashMap.put("status",statusNew);

                        dbref.child(UID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {



                                //list.remove(pos);
                                adapter.notifyItemRemoved(pos);
                                adapter.notifyItemRangeChanged(pos, list.size());

                               // ctxt.startActivity(new Intent(AdminPendingShops.this,AdminPendingShops.class));
                                Toast.makeText(AdminPendingShops.this, "Approved Done Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminPendingShops.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        alertbox.show();
    }

//    @Override
//    public void onBackPressed() {
//        this.finish();
//        startActivity(new Intent(AdminPendingShops.this,AdminDashbrd.class));
//        super.onBackPressed();
//    }
}