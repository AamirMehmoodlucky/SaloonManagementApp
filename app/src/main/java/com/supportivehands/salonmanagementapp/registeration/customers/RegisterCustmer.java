package com.supportivehands.salonmanagementapp.registeration.customers;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.supportivehands.salonmanagementapp.R;
import com.supportivehands.salonmanagementapp.databinding.ActivityRegisterCustmerBinding;
import com.supportivehands.salonmanagementapp.model.modelRegistr;

import com.supportivehands.salonmanagementapp.registeration.RegisterFormTwo;

public class RegisterCustmer extends AppCompatActivity {
    ActivityRegisterCustmerBinding binding;
    String  strname,strmobileNo,stremail,strpass,strcpass;
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
    ModelRegistrCustmer modelRegistrCustr;
    //image uri dp..that pass from previous

    //progress for uploading data pic etc
    ProgressDialog pd;
    String uid;
    public  static String usertype;
    Uri uriPik;
    Intent intentImage;
    String str_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding=ActivityRegisterCustmerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();    //Getting instance of FirebaseAuth
        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("Custmers");
        binding.register.setOnClickListener(view -> {
            strname=binding.ownName.getText().toString();
             strmobileNo=binding.mobileno.getText().toString();
             stremail=binding.email.getText().toString();
             strpass=binding.paswrd.getText().toString();
            strcpass=binding.cpasward.getText().toString();

            if (strname.isEmpty()){
                binding.email.setError("Enter Name");
            }else if (strmobileNo.isEmpty()){
                binding.email.setError("Enter mobile no");
            }else if (stremail.isEmpty()){
                binding.email.setError("Enter Email");
            }else if (strpass.isEmpty()){
                binding.paswrd.setError("Enter Password");
            }else if (strcpass.isEmpty()){
                binding.cpasward.setError("Enter Confirm Pasword");
            }else if (!strpass.equals(strcpass)){
                Toast.makeText(this, "Password not matched", Toast.LENGTH_SHORT).show();
            } else {
                modelRegistrCustr=new ModelRegistrCustmer("link",strname,strmobileNo,stremail,uid,strpass);
                auth.fetchSignInMethodsForEmail(stremail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        // Toast.makeText(RegisterFormTwo.this, "1", Toast.LENGTH_SHORT).show();

                        if (task.getResult().getSignInMethods().size() == 0){
                            // email not existed
                            // registerUser();
                            // uploadPhotoInFirebase(Uri.parse(imge));
                            //  Toast.makeText(RegisterFormTwo.this, "ernterd", Toast.LENGTH_SHORT).show();
                            uploadPhoto(Uri.parse(str_image) , strname+"_"+strmobileNo);
                        }else {
                            // email existed
                            Toast.makeText(RegisterCustmer.this, "Email Already Exist", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }

        });
        //profile image pick
        binding.idprofileimge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        someActivityResultLauncher.launch(intent);
                Toast.makeText(RegisterCustmer.this, "Pick Profile Image", Toast.LENGTH_SHORT).show();

            }
        });
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        intentImage = result.getData();
                        uriPik=intentImage.getData();
                        str_image=intentImage.getData().toString();

                        binding.idprofileimge.setImageURI(intentImage.getData());
                    }
                }
            });
    private void uploadPhoto(Uri file, String name){
        pd=new ProgressDialog(this);
        pd.setTitle("Please wait");
        pd.show();
        // Create a reference to "mountains.jpg"
        StorageReference riversRef = storageReference.child("profilesUsers/"+name+".png");

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
                Toast.makeText(RegisterCustmer.this, "ImageFaild:"+e.getMessage(), Toast.LENGTH_SHORT).show();



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
        Toast.makeText(this, "register entr call", Toast.LENGTH_SHORT).show();
        binding.progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(modelRegistrCustr.getEmail(), modelRegistrCustr.getPass())
                .addOnCompleteListener(RegisterCustmer.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegisterCustmer.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                        String UID= task.getResult().getUser().getUid();



                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterCustmer.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_LONG).show();
                            Log.e("MyTag", task.getException().toString());
                        } else {

                            addDatatoFireBase(imgelink,strname,strmobileNo,stremail,strpass,UID);


                        }
                    }
                });
    }
    private void addDatatoFireBase(String link, String name, String mobileno, String email, String pass, String uid) {

        modelRegistrCustr.setPropfilepic(link);
        modelRegistrCustr.setName(name);
        modelRegistrCustr.setMobie(mobileno);
        modelRegistrCustr.setEmail(email);
        modelRegistrCustr.setPass(pass);
        modelRegistrCustr.setUid(uid);


        String key = databaseReference.push().getKey();

        databaseReference.child(uid).setValue(modelRegistrCustr);


        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                pd.dismiss();
                startActivity(new Intent(RegisterCustmer.this, LoginCustmer.class));
                finish();
                Toast.makeText(RegisterCustmer.this, "User Registered", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(RegisterCustmer.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                Log.d("aamir","errorindataupload="+error.getMessage());
            }
        });
    }

}