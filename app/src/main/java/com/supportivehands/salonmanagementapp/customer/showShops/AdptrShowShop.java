package com.supportivehands.salonmanagementapp.customer.showShops;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.supportivehands.salonmanagementapp.admin.ModelPendingShop.ShopInterface;
import com.supportivehands.salonmanagementapp.admin.ModelPendingShop.UserProvider;
import com.supportivehands.salonmanagementapp.shopservices.CustmerShowServices;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdptrShowShop extends RecyclerView.Adapter<AdptrShowShop.MyViewHolder>{

    Context context;
    ArrayList<UserProvider> list;
    String AdptrStatus;
    ShopInterface shopInterface;

    public AdptrShowShop(Context context, ArrayList<UserProvider> list, String AdptrStatus) {
        this.context = context;
        this.list = list;
        this.AdptrStatus=AdptrStatus;
        this.shopInterface=shopInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.itemshowshopp,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserProvider model=list.get(position);


        holder.tvShopName.setText(" "+model.getShopName());
        holder.tvMobile.setText(model.getMobileNo());
        holder.tvAdress.setText(model.getAdress());
        holder.tvStatus.setText(model.getStatus());
        String UID=model.getUid();



        Glide.with(context).load(model.getLgo()).into(holder.ivDp);

        //tracking on google map
        holder.tvTrackmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+model.getLat()+","+model.getLng()+"&daddr="+model.getLat()+","+model.getLng();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(Intent.createChooser(intent, "Select an application"));
            }
        });
        //show service button click
        holder.btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustmerShowServices.uid =model.getUid();
                context.startActivity(new Intent(context, CustmerShowServices.class));
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvShopName,tvMobile,tvAdress,tvStatus,tvTrackmap;
        ImageView ivDp;
        MaterialButton btnService;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvShopName=itemView.findViewById(R.id.id_shopsname);

            tvMobile=itemView.findViewById(R.id.id_mobile);
            tvAdress=itemView.findViewById(R.id.idadress);
            tvStatus=itemView.findViewById(R.id.idstatuss);
            tvTrackmap=itemView.findViewById(R.id.idtrack);
            ivDp=itemView.findViewById(R.id.id_dp);

            btnService=itemView.findViewById(R.id.idbtnService);



        }
    }
    }