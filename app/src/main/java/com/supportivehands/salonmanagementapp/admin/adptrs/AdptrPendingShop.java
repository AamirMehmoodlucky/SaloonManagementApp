package com.supportivehands.salonmanagementapp.admin.adptrs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.supportivehands.salonmanagementapp.R;
import com.supportivehands.salonmanagementapp.admin.ModelPendingShop.ModelPendingShp;
import com.supportivehands.salonmanagementapp.admin.ModelPendingShop.ShopInterface;
import com.supportivehands.salonmanagementapp.admin.ModelPendingShop.UserProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdptrPendingShop extends RecyclerView.Adapter<AdptrPendingShop.MyViewHolder>{

    Context context;
    ArrayList<UserProvider> list;
    String AdptrStatus;
    ShopInterface shopInterface;

    public AdptrPendingShop(Context context, ArrayList<UserProvider> list,String AdptrStatus,ShopInterface shopInterface) {
        this.context = context;
        this.list = list;
        this.AdptrStatus=AdptrStatus;
        this.shopInterface=shopInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.itemshopp,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserProvider model=list.get(position);

        holder.tvOwnerName.setText(model.getOwnerName());
        holder.tvShopName.setText(" Shop Name: "+model.getShopName());
        holder.tvEmail.setText(" Email: "+model.getEmail());
        holder.tvMobile.setText(" Mobile No: "+model.getMobileNo());
        holder.tvCity.setText(" City: "+model.getCity());
        holder.tvAdress.setText(" Adress: "+model.getAdress());
        holder.tvMapAdress.setText(" Map Adress: "+model.getMapAdress());
        holder.tvStatus.setText(model.getStatus());
        String UID=model.getUid();

        if (AdptrStatus.equals("Pending")){
            holder.btnApprove.setText("Approve this Shop");
        }else if (AdptrStatus.equals("Approved")){
            holder.btnApprove.setText("Put in Pending");
        }

        Glide.with(context).load(model.getLgo()).into(holder.ivDp);
       int poss=position;
        //yes no alert diague to accpt or not
        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AdptrStatus.equals("Pending")){
//                    showAlertDiag(UID,poss,"Approved",view);
                     shopInterface.reomveAndUpdate(UID,poss,"Approved",view.getContext());


                }else if (AdptrStatus.equals("Approved")){
//                    showAlertDiag(UID,poss,"Pending",view);
                    shopInterface.reomveAndUpdate(UID,poss,"Pending",view.getContext());

                }

            }
        });
        //tracking on google map
        holder.tvTrackmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+model.getLat()+","+model.getLng()+"&daddr="+model.getLat()+","+model.getLng();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(Intent.createChooser(intent, "Select an application"));
            }
        });

    }

    private void showAlertDiag(String UID,int pos,String AdptrStatus,View view) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
        if (AdptrStatus.equals("Pending")){
            alertbox.setMessage("Are you sure you want to put this shop in Pending?");
        }else if (AdptrStatus.equals("Approved")){
            alertbox.setMessage("Are you sure you want to approve this shop?");

        }

        alertbox.setTitle("Alert..!");
        alertbox.setIcon(R.drawable.logo);

        alertbox.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,
                                        int arg1) {

                        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap hashMap=new HashMap();
                        hashMap.put("status",AdptrStatus);

                        dbref.child(UID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {


                                list.remove(pos);
                                notifyItemRemoved(pos);
                                notifyItemChanged(pos);
                                notifyItemRangeChanged(pos,list.size());

//                                notifyDataSetChanged();


                                Toast.makeText(context, "Done Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvOwnerName,tvShopName,tvEmail,tvMobile,tvCity,tvAdress,tvMapAdress,tvStatus,tvTrackmap;
        CircleImageView ivDp;
        MaterialButton btnApprove;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
             tvOwnerName=itemView.findViewById(R.id.id_name);
            tvShopName=itemView.findViewById(R.id.id_shopname);
            tvEmail=itemView.findViewById(R.id.idemailuser);
            tvMobile=itemView.findViewById(R.id.idmobileno);
            tvCity=itemView.findViewById(R.id.idcity);
            tvAdress=itemView.findViewById(R.id.idadres);
            tvMapAdress=itemView.findViewById(R.id.idmapadress);
            tvStatus=itemView.findViewById(R.id.idstatuss);

            tvTrackmap=itemView.findViewById(R.id.idtrack);

            ivDp=itemView.findViewById(R.id.id_dp);

            btnApprove=itemView.findViewById(R.id.idbtnApproveShop);



        }
    }
    }