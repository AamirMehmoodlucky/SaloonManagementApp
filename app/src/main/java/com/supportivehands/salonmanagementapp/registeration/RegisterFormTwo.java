package com.supportivehands.salonmanagementapp.registeration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.supportivehands.salonmanagementapp.databinding.ActivityRegisterFormTwoBinding;
import com.supportivehands.salonmanagementapp.model.modelRegistr;

import java.util.Objects;

public class RegisterFormTwo extends AppCompatActivity {
    ActivityRegisterFormTwoBinding binding;
    String  str_shopnam,str_nam,str_mob,str_adres,str_city,str_mapAdres,lat,lng,imge;
    FirebaseAuth auth;  //FirebaseAuth Instance
    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseStorage storage;

    // creating a variable for
    // our object class
    modelRegistr modelR;
    //image uri dp..that pass from previous

    //progress for uploading data pic etc
     ProgressDialog pd;
     String uid;
    public  static String usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterFormTwoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();    //Getting instance of FirebaseAuth
        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("Users");



        // initializing our object
        // model class variable and instance.
        modelR = new modelRegistr();

        Intent intent=getIntent();

        str_shopnam=intent.getStringExtra("shopname");
        str_nam=intent.getStringExtra("name");
        str_mob=intent.getStringExtra("mob");
        str_adres=intent.getStringExtra("adrs");
        str_city=intent.getStringExtra("city");
        str_mapAdres=intent.getStringExtra("mapadress");
        lat=intent.getStringExtra("lati");
        lng=intent.getStringExtra("lngi");
        imge=intent.getStringExtra("image");
        binding.logomain.setImageURI(Uri.parse(imge));



        Log.d("aamir",str_shopnam);
        Log.d("aamir",str_nam);
        Log.d("aamir",str_mob);
        Log.d("aamir",str_adres);
        Log.d("aamir",str_city);
        Log.d("aamir",str_mapAdres);
        Log.d("aamir",lat);
        Log.d("aamir",lng);

        binding.register.setOnClickListener(view -> {
            String email=binding.email.getText().toString();
            String pass=binding.paswrd.getText().toString();
            String cpass=binding.cpasward.getText().toString();

            if (email.isEmpty()){
                binding.email.setError("Enter Email");
            }else if (pass.isEmpty()){
                binding.paswrd.setError("Enter Password");
            }else if (cpass.isEmpty()){
                binding.cpasward.setError("Enter Confirm Pasword");
            }else if (!pass.equals(cpass)){
                Toast.makeText(this, "Password not matched", Toast.LENGTH_SHORT).show();
            }else if (pass.length() < 6){
                Toast.makeText(this, "Password must be greater the 6-Digits", Toast.LENGTH_SHORT).show();
            } else {
                modelR=new modelRegistr("link",str_shopnam,str_nam,str_mob,str_adres,str_city,str_mapAdres,lat,lng,email,pass,uid,usertype,"Pending");
                auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                       // Toast.makeText(RegisterFormTwo.this, "1", Toast.LENGTH_SHORT).show();

                        if (task.getResult().getSignInMethods().size() == 0){
                            // email not existed
                           // registerUser();
                           // uploadPhotoInFirebase(Uri.parse(imge));
                          //  Toast.makeText(RegisterFormTwo.this, "ernterd", Toast.LENGTH_SHORT).show();
                            uploadPhoto(Uri.parse(imge) , str_nam+"_"+str_shopnam);
                        }else {
                            // email existed
                            Toast.makeText(RegisterFormTwo.this, "Email Already Exist", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }

        });



    }

    private void uploadPhoto(Uri file, String name){
        pd=new ProgressDialog(this);
        pd.setTitle("Please wait");
        pd.show();
        // Create a reference to "mountains.jpg"
        StorageReference riversRef = storageReference.child("profiles/"+name+".png");

        riversRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String link=task.getResult().toString();
                        registerUser(link);
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(RegisterFormTwo.this, "ImageFaild:"+e.getMessage(), Toast.LENGTH_SHORT).show();



            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercentage=(100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Data uploading: "+ (int) progressPercentage + "%");
            }
        });




    }

    private void registerUser(String imgelink) {
        binding.progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(modelR.getEmail(), modelR.getPas())
                        .addOnCompleteListener(RegisterFormTwo.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(RegisterFormTwo.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                binding.progressBar.setVisibility(View.GONE);
                                String UID= task.getResult().getUser().getUid();



                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterFormTwo.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_LONG).show();
                                    Log.e("MyTag", task.getException().toString());
                                } else {

                                    addDatatoFireBase(imgelink,str_shopnam,str_nam,str_mob,str_adres,str_city,str_mapAdres,lat,lng,modelR.getEmail(),modelR.getEmail(),UID);


                                }
                            }
                        });
    }

    private void addDatatoFireBase(String link, String str_shopnam, String str_nam, String str_mob, String str_adres, String str_city, String str_mapAdres, String lat, String lng, String email, String pass,String uid) {

        modelR.setLgo(link);
        modelR.setShopName(str_shopnam);
        modelR.setOwnerName(str_nam);
        modelR.setMobileNo(str_mob);
        modelR.setAdress(str_adres);
        modelR.setCity(str_city);
        modelR.setMapAdress(str_mapAdres);
        modelR.setLat(lat);
        modelR.setLng(lng);
        modelR.setEmail(email);
        modelR.setPas(pass);
        modelR.setUid(uid);
        modelR.setUserType(usertype);
        modelR.setStatus("Pending");
        String key = databaseReference.push().getKey();
        databaseReference.child(uid).setValue(modelR);


        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                pd.dismiss();
                startActivity(new Intent(RegisterFormTwo.this, LoginActivity.class));
                finish();
                Toast.makeText(RegisterFormTwo.this, "User Registered", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(RegisterFormTwo.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                Log.d("aamir","errorindataupload="+error.getMessage());
            }
        });
    }

    // Checking the current all state
    @Override
    public void onStart() {
        super.onStart();

        // if user logged in, go to sign-in screen
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        binding.progressBar.setVisibility(View.GONE);
    }
}