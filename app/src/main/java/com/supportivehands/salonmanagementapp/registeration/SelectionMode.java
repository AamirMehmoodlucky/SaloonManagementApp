package com.supportivehands.salonmanagementapp.registeration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.supportivehands.salonmanagementapp.R;
import com.supportivehands.salonmanagementapp.admin.AdminDashbrd;
import com.supportivehands.salonmanagementapp.admin.AdminLogin;
import com.supportivehands.salonmanagementapp.databinding.ActivitySelectionModeBinding;
import com.supportivehands.salonmanagementapp.registeration.customers.LoginCustmer;

import static com.supportivehands.salonmanagementapp.common.Common.checkPermision;

public class SelectionMode extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private ActivitySelectionModeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySelectionModeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSaloonOwnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermision(SelectionMode.this,getPackageName());

            }
        });




        binding.btnCustmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectionMode.this, LoginCustmer.class));

            }
        });
        binding.idmenutop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(SelectionMode.this,view);
                popupMenu.setOnMenuItemClickListener(SelectionMode.this);
                popupMenu.inflate(R.menu.menu_file);
                popupMenu.show();
            }
        });

    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
       switch (menuItem.getItemId()){
           case R.id.idmenu:
               startActivity(new Intent(SelectionMode.this, AdminLogin.class));
               return true;
           default:
               return false;
       }
    }
}