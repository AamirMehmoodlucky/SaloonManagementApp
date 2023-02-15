package com.supportivehands.salonmanagementapp.shopservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.supportivehands.salonmanagementapp.OwnerDashboard.OwnerSellerDashbrd;
import com.supportivehands.salonmanagementapp.databinding.ActivityShopServicesBinding;
import com.supportivehands.salonmanagementapp.model.ModelAddService;

import java.util.Objects;

import static com.supportivehands.salonmanagementapp.OwnerDashboard.OwnerSellerDashbrd.lat;
import static com.supportivehands.salonmanagementapp.OwnerDashboard.OwnerSellerDashbrd.lng;
import static com.supportivehands.salonmanagementapp.OwnerDashboard.OwnerSellerDashbrd.mobileSeller;

public class ShopServices extends AppCompatActivity {
    private ActivityShopServicesBinding binding;
    public static final int PICK_IMAGE = 1;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;
    FirebaseStorage storage;
    FirebaseAuth auth;


    ProgressDialog pd;
    Uri uri;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityShopServicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//database reference
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Services");
        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        auth=FirebaseAuth.getInstance();
        uid= Objects.requireNonNull(auth.getCurrentUser()).getUid();


        //pick service image
        binding.idserviceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        binding.idAddServic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String title=binding.idservicetitle.getText().toString();
               String price=binding.idpricepefrusr.getText().toString();
               String description=binding.idDescription.getText().toString();
                String availbility = null;
                if (binding.idradioYes.isChecked()){
                    availbility="Yes";
                }if (binding.idradioNo.isChecked()){
                    availbility="No";
                }
               uploadPhoto(uri,title,price,description,availbility);


            }
        });

    }
    //upload service image
    private void uploadPhoto(Uri file, String title,String price,String description,String status){
        pd=new ProgressDialog(this);
        pd.setTitle("Please wait");
        pd.show();
        // Create a reference to "mountains.jpg"
        StorageReference riversRef = storageReference.child("services/"+title+".png");

        riversRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String link=task.getResult().toString();
                        addServiceData(title,price,description,status,link);
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(ShopServices.this, "ImageFaild:"+e.getMessage(), Toast.LENGTH_SHORT).show();



            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercentage=(100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Service Uploading: "+ (int) progressPercentage + "%");
            }
        });




    }
    //uploading data
    private void addServiceData(String title,String price,String des, String status, String link) {
        String key = databaseReference.push().getKey();
        ModelAddService modelAddService=new ModelAddService();
        modelAddService.setTitle(title);
        modelAddService.setPrice(price);
       modelAddService.setDes(des);
       modelAddService.setStatus(status);
       modelAddService.setLink(link);
       modelAddService.setUid(uid);
       modelAddService.setLat(lat);
       modelAddService.setLng(lng);
       modelAddService.setPushKey(key);
       modelAddService.setSellerMobile(mobileSeller);

        databaseReference.child(key).setValue(modelAddService);


        databaseReference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pd.dismiss();
                startActivity(new Intent(ShopServices.this, OwnerSellerDashbrd.class));
                Toast.makeText(ShopServices.this, "Service added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(ShopServices.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    //result of pickimage and set in image view first
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
             uri = data.getData();
           binding.idserviceImage.setImageURI(uri);
        }
    }
}