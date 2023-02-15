package com.supportivehands.salonmanagementapp.shopservices;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.supportivehands.salonmanagementapp.R;
import com.supportivehands.salonmanagementapp.customer.CustmerDashBoard;
import com.supportivehands.salonmanagementapp.model.ModelAddService;
import com.supportivehands.salonmanagementapp.orders.ModelOrder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.supportivehands.salonmanagementapp.customer.CustmerDashBoard.mobileBuyer;

public class AdapterServicesShow extends RecyclerView.Adapter<AdapterServicesShow.MyViewHolder>{

    Context context;
    ArrayList<ModelAddService> list;
    String buyerUid;



    public AdapterServicesShow(Context context, ArrayList<ModelAddService> list,String buyerUID) {
        this.context = context;
        this.list = list;
        this.buyerUid=buyerUID;


    }


    @NonNull
    @Override
    public AdapterServicesShow.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.itemserviceshopcustmer,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterServicesShow.MyViewHolder holder, int position) {
        ModelAddService model=list.get(position);
        holder.tvTitle.setText(model.getTitle());
        holder.tvprice.setText(" Rs: "+model.getPrice() +" only / Service");
        holder.tvDes.setText("Description : "+model.getDes());
        Glide.with(context).load(model.getLink()).into(holder.ivDp);


        int pos=position;

        holder.btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrderPopup(pos,context,model.getTitle(),model.getLink(),model.getPrice(),model.getLat(),model.getLng(),"Pending",buyerUid,model.getUid(),model.getSellerMobile());
            }
        });

    }

    private void placeOrderPopup(int pos,Context ctxt,String title,String serviceUrl,String price,String lati,String lngi,String orderstatus, String uidbuyer, String uidseller,String mobileSeller ) {

            AlertDialog.Builder alertbox = new AlertDialog.Builder(ctxt);
            View viewDialog=LayoutInflater.from(ctxt).inflate(R.layout.itemlaout_dialog,null);
            TextView tvStartTime,tvEndTime;
        Button btnConfirm;
        tvStartTime=viewDialog.findViewById(R.id.idstarttime);
        tvEndTime=viewDialog.findViewById(R.id.idendtime);

        btnConfirm=viewDialog.findViewById(R.id.idbtnconfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sttime=tvStartTime.getText().toString();
                String endtime=tvEndTime.getText().toString();
                if (sttime.isEmpty()){
                    Toast.makeText(ctxt, "Enter Start of your Availibilty Time", Toast.LENGTH_SHORT).show();
                }else if (endtime.isEmpty()){
                    Toast.makeText(ctxt, "Enter End of your Availibilty Time", Toast.LENGTH_SHORT).show();
                }else {
                    uploadOrder(title, serviceUrl, price, lati, lngi, orderstatus, uidbuyer, uidseller, mobileSeller,sttime, endtime);
                }
            }
        });
        alertbox.setView(viewDialog);
        alertbox.setCancelable(true);
        alertbox.show();


//            alertbox.setMessage("Are you sure you want place Order?");
//
//
//            alertbox.setTitle("Confirmation..!");
//            alertbox.setIcon(R.drawable.logo);
//
//            alertbox.setPositiveButton("Yes",
//                    new DialogInterface.OnClickListener() {
//
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            uploadOrder( title, serviceUrl, price, lati, lngi, orderstatus,  uidbuyer,  uidseller ,mobileSeller);
//
//                        }
//                    });
//            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//
//                }
//            });
//            alertbox.show();


    }

    private void uploadOrder(String title,String serviceUrl,String price,String lati,String lngi,String orderstatus, String uidbuyer, String uidseller ,String mobileSeller,String starttime,String endtime) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Orderslist");
        String key = databaseReference.push().getKey();

        ModelOrder modelOrder=new ModelOrder();
        modelOrder.setTitle(title);
        modelOrder.setUrl(serviceUrl);
        modelOrder.setPrice(price);
        modelOrder.setLat(lati);
        modelOrder.setLng(lngi);
        modelOrder.setOrderstatus(orderstatus);
        modelOrder.setUidbuyer(uidbuyer);
        modelOrder.setUidseller(uidseller);
        modelOrder.setMobileSeller(mobileSeller);
        modelOrder.setMobileBuyer(mobileBuyer);
        modelOrder.setStrttime(starttime);
        modelOrder.setEndtime(endtime);
        modelOrder.setPushkey(key);

        databaseReference.child(key).setValue(modelOrder);


        databaseReference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                context.startActivity(new Intent(context, CustmerDashBoard.class));
                Toast.makeText(context, "Order Placed Successfully..!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(context, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
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