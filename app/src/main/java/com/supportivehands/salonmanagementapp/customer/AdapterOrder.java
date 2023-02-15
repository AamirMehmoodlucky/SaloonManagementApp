package com.supportivehands.salonmanagementapp.customer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.supportivehands.salonmanagementapp.OwnerDashboard.OrderRecieved;
import com.supportivehands.salonmanagementapp.R;
import com.supportivehands.salonmanagementapp.orders.ModelOrder;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.MyViewHolder>{

    Context context;
    ArrayList<ModelOrder> list;
    String activityStatus;



    public AdapterOrder(Context context, ArrayList<ModelOrder> list,String activityStatus) {
        this.context = context;
        this.list = list;
        this.activityStatus=activityStatus;


    }


    @NonNull
    @Override
    public AdapterOrder.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.itemorderlist,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrder.MyViewHolder holder, int position) {
        ModelOrder model=list.get(position);
        holder.tvTitle.setText(model.getTitle());
        holder.tvprice.setText("Rs: "+model.getPrice());
        holder.tvorderstatus.setText("Order Status: "+model.getOrderstatus());
        holder.tvtimging.setText("Availbility time\n From: "+model.getStrttime()+", To: "+model.getEndtime());
        int pos=position;
        Glide.with(context).load(model.getUrl()).into(holder.ivDpService);
        if (activityStatus.equals("1")){
            holder.btnTrack.setText("Track");
            holder.tvmobile.setText(model.getMobileSeller() +"(Seller Contact)");
            holder.bottmlayout.setVisibility(View.GONE);
        }else if (activityStatus.equals("2")){
            holder.btnTrack.setText("Me");
            holder.tvmobile.setText(model.getMobileBuyer() +"(Client Contact)");
            holder.bottmlayout.setVisibility(View.VISIBLE);
        }
        if (model.getOrderstatus().equals("Accepted") || model.getOrderstatus().equals("Rejected")){
            holder.bottmlayout.setVisibility(View.GONE);
        }

        //accpt click
        holder.btnAccpetOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread( new Runnable() { @Override public void run() {
                    // Run whatever background code you want here.
                    orderAcceptRejcet(model.getPushkey(),pos,"Accepted");
                } } ).start();




            }
        });
        holder.btnRejectOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread( new Runnable() { @Override public void run() {
                    // Run whatever background code you want here.
                    orderAcceptRejcet(model.getPushkey(),pos,"Rejected");
                } } ).start();
            }
        });
        //tracking on google map
        holder.btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+model.getLat()+","+model.getLng()+"&daddr="+model.getLat()+","+model.getLng();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(Intent.createChooser(intent, "Select an application"));

            }
        });
    }

    private void orderAcceptRejcet(String puskey,int pos,String AcptReject) {
        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference().child("Orderslist");
        HashMap hashMap=new HashMap();
        hashMap.put("orderstatus",AcptReject);

        dbref.child(puskey).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                notifyItemChanged(pos);
                context.startActivity(new Intent(context,OrderRecieved.class));
                Toast.makeText(context, "Order Accepted Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvprice,tvmobile,tvorderstatus,tvtimging;
        ImageView ivDpService;
        MaterialButton btnTrack;
        RelativeLayout bottmlayout;
        Button btnAccpetOrder,btnRejectOrder;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle=itemView.findViewById(R.id.idtitle);
            tvprice=itemView.findViewById(R.id.idprice);
            tvmobile=itemView.findViewById(R.id.idmobile);
            tvorderstatus=itemView.findViewById(R.id.idstatus);
            tvtimging=itemView.findViewById(R.id.idtimeing);
            ivDpService=itemView.findViewById(R.id.idservice);
            btnTrack=itemView.findViewById(R.id.idtrck);
            bottmlayout=itemView.findViewById(R.id.idbottomLayout);
            btnAccpetOrder=itemView.findViewById(R.id.idbtnAccept);
            btnRejectOrder=itemView.findViewById(R.id.idbtnReject);
        }
    }

}