package com.supportivehands.salonmanagementapp.OwnerDashboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.supportivehands.salonmanagementapp.R;

import com.supportivehands.salonmanagementapp.model.ModelAddService;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterServices extends RecyclerView.Adapter<AdapterServices.MyViewHolder>{

    Context context;
    ArrayList<ModelAddService> list;



    public AdapterServices(Context context, ArrayList<ModelAddService> list) {
        this.context = context;
        this.list = list;


    }


    @NonNull
    @Override
    public AdapterServices.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.itemserviceshop,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterServices.MyViewHolder holder, int position) {
        ModelAddService model=list.get(position);
        holder.tvTitle.setText(model.getTitle());
        holder.tvprice.setText(" Rs: "+model.getPrice());
        holder.tvDes.setText("Description : "+model.getDes());
        Glide.with(context).load(model.getLink()).into(holder.ivDp);
        holder.btnStatus.setText(model.getStatus());
        int pos=position;

        holder.btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, OwnerSellerDashbrd.class));
                if (model.getStatus().equals("Yes")){

                DatabaseReference dbref= FirebaseDatabase.getInstance().getReference().child("Services");
                HashMap hashMap=new HashMap();
                hashMap.put("status","No");

                dbref.child(model.getPushKey()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {

                       // context.startActivity(new Intent(context,OwnerDashbrd.class));
                        notifyItemChanged(pos);
                        Toast.makeText(context, "Service Invisble to User", Toast.LENGTH_SHORT).show();
                        holder.btnStatus.setText("No");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                }else if (model.getStatus().equals("No")){
                    DatabaseReference dbref= FirebaseDatabase.getInstance().getReference().child("Services");
                    HashMap hashMap=new HashMap();
                    hashMap.put("status","Yes");

                    dbref.child(model.getPushKey()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            notifyItemChanged(pos);
                            Toast.makeText(context, "Service Availeble to All", Toast.LENGTH_SHORT).show();
                            holder.btnStatus.setText("Yes");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvDes,tvprice;
                ImageView ivDp;
        MaterialButton btnStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.id_title);
                tvDes=itemView.findViewById(R.id.id_des);
                tvprice=itemView.findViewById(R.id.idprice);



                ivDp=itemView.findViewById(R.id.id_dpservice);

            btnStatus=itemView.findViewById(R.id.idStatusServicve);
        }
    }

}