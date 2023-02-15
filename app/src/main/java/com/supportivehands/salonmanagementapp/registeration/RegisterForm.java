package com.supportivehands.salonmanagementapp.registeration;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.supportivehands.salonmanagementapp.R;
import com.supportivehands.salonmanagementapp.databinding.ActivityRegisterFormBinding;

import java.util.ArrayList;

import static com.supportivehands.salonmanagementapp.common.Common.showToast;

public class RegisterForm extends AppCompatActivity {
    ActivityRegisterFormBinding binding;
    Intent intentImage;
    String shopName,ownerName,mobileNo,adress,cityName,lat,lng;
   // String uriString;
    String str_image;
    Uri uriPik;
    String  str_shopnam,str_nam,str_mob,str_adres,str_city,str_mapAdres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i=getIntent();
        lat=i.getStringExtra("latitude");
        lng=i.getStringExtra("logitude");
          str_shopnam=i.getStringExtra("shopnam");
          str_nam=i.getStringExtra("personname");
          str_mob=i.getStringExtra("mobile");
         str_adres=i.getStringExtra("address");
         str_city=i.getStringExtra("city");
         str_mapAdres=i.getStringExtra("mapAdress");
         str_image=i.getStringExtra("imageBack");

        //adding city in spiner
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select City");
        arrayList.add("Abbottabad");
        arrayList.add("Havelian");
        arrayList.add("Mansehra");
        arrayList.add("Huripur");
        arrayList.add("Peshawar");
        arrayList.add("Islamabad");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item
                , arrayList);
        binding.spinner1.setAdapter(arrayAdapter);
        binding.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_city = parent.getItemAtPosition(position).toString();
                ((TextView) view).setTextColor(getResources().getColor(R.color.golden));

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });


        if (str_shopnam != null){
            binding.shopName.setText(str_shopnam);
        }if (str_nam != null){
            binding.ownerName.setText(str_nam);
        }if (str_mob != null){
            binding.mobileno.setText(str_mob);
        }if (str_adres != null){
            binding.address.setText(str_adres);
        }if (str_city != null){
            binding.spinner1.setSelection(1);
        }if (str_mapAdres != null){
            binding.businessAdress.setText(str_mapAdres);
        }if (str_image != null){
            binding.logo.setImageURI(Uri.parse(str_image));
        }
      //  Toast.makeText(this, ""+ str_lat +","+str_log+","+str_shopnam+","+str_nam+","+str_adres+","+str_city, Toast.LENGTH_SHORT).show();



        binding.logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                someActivityResultLauncher.launch(intent);

            }
        });


        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopName = binding.shopName.getText().toString();
                ownerName = binding.ownerName.getText().toString();
               adress = binding.address.getText().toString();
                cityName = str_city;
                mobileNo=binding.mobileno.getText().toString();

                if (  str_image == null ) {
                    showToast(RegisterForm.this,"Upload Logo");
                } else if (shopName.isEmpty()) {
                    binding.shopName.setError("Enter Shop Name");
                }else if (ownerName.isEmpty()) {
                    binding.ownerName.setError("Enter Business Owner Name");
                }else if (mobileNo.isEmpty()){
                    binding.mobileno.setError("Enter Mobile No..!");
                }else if (adress.isEmpty()) {
                    binding.address.setError("Enter Business Adress");
                }else if (cityName.isEmpty()) {
                    Toast.makeText(RegisterForm.this, "Select City", Toast.LENGTH_SHORT).show();
                }else if (lat == null && lng == null) {
                    binding.businessAdress.setError("Pick Location");
                }else{
                    Intent intent=new Intent(RegisterForm.this,RegisterFormTwo.class);
//                    String  str_shopnam,str_nam,str_mob,str_adres,str_city,str_mapAdres;
                    intent.putExtra("shopname",str_shopnam);
                    intent.putExtra("name",str_nam);
                    intent.putExtra("mob",str_mob);
                    intent.putExtra("adrs",str_adres);
                    intent.putExtra("city",str_city);
                    intent.putExtra("mapadress",str_mapAdres);
                    intent.putExtra("lati",lat);
                    intent.putExtra("lngi",lng);
                    intent.putExtra("image",str_image);
                    startActivity(intent);


                }


            }
        });
        binding.businessAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopName = binding.shopName.getText().toString();
                ownerName = binding.ownerName.getText().toString();
                adress = binding.address.getText().toString();
                cityName =str_city;
                mobileNo=binding.mobileno.getText().toString();

                if (str_image == null) {
                    showToast(RegisterForm.this,"Upload Logo");
                } else if (shopName.isEmpty()) {
                    binding.shopName.setError("Enter Shop Name");
                }else if (ownerName.isEmpty()) {
                    binding.ownerName.setError("Enter Business Owner Name");
                }else if (mobileNo.isEmpty()){
                    binding.mobileno.setError("Enter Mobile No");
                }else if (adress.isEmpty()) {
                    binding.address.setError("Enter Business Adress");
                }else if (cityName.isEmpty()) {
                    Toast.makeText(RegisterForm.this, "Select City", Toast.LENGTH_SHORT).show();
                }else {
                   // goToo(RegisterForm.this,pickLocation.class);
                    Intent intent=new Intent(RegisterForm.this,pickLocation.class);
                    intent.putExtra("shopname",shopName);
                    intent.putExtra("ownername",ownerName);
                    intent.putExtra("mobileNo",mobileNo);
                    intent.putExtra("address",adress);
                    intent.putExtra("cityname",cityName);
                    intent.putExtra("image",str_image);
                    startActivity(intent);
                }

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

                        binding.logo.setImageURI(intentImage.getData());
                    }
                }
            });
}