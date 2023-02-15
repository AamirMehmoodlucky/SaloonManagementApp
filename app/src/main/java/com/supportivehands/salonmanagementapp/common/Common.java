package com.supportivehands.salonmanagementapp.common;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.supportivehands.salonmanagementapp.registeration.LoginActivity;
import com.supportivehands.salonmanagementapp.registeration.RegisterForm;

import static com.supportivehands.salonmanagementapp.registeration.RegisterFormTwo.usertype;

public class Common {
    public static void showToast(Context context,String message){
        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
    }
    public static void goToo(Context context, Class clas){
        context.startActivity(new Intent(context,clas));
    }
    public static void checkPermision(Context context,String pakge) {
        Dexter.withContext(context).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                goToo(context, LoginActivity.class);
                usertype="ServiceProvider";
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent=new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri=Uri.fromParts("package",pakge,null);
                intent.setData(uri);
                context.startActivity(intent);

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
}
