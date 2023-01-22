package com.example.autoclicker1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    ActivityResultLauncher<Intent> startforResult1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) { //When an Activity first call or launched then onCreate(Bundle savedInstanceState) method is responsible to create the activity.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//sets the XML file you want as your main layout when the app starts.
        if(!Settings.canDrawOverlays(this)){
            askPermission();
        }
        findViewById(R.id.startFloat).setOnClickListener(this);//Finds a view that was identified by the id attribute from the XML layout resource

    }
    private void askPermission(){
        Intent intent1=new Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,//settings permission to cover over other apps
                Uri.parse (String.valueOf("package:"+getPackageName()))//uri(package:com.test.autoclicker1)
        );//intent to perform an action on the screen,to start activity,send message between two activities
        //startActivityForResult(intent1,SYSTEM_ALERT_WINDOW_PERMISSION);
        startforResult1.launch(intent1);
    }

    @Override
    public void onClick(View view) {
        if(Settings.canDrawOverlays(this)){
            startService(new Intent(MainActivity.this,FloatingView.class));
            finish();//Dismiss any dialogs the activity was managing.
                    //Close any cursors the activity was managing.
            //Close any open search dialog
        }
        else{
            Toast.makeText(this,
                    "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
            askPermission();
        }
    }
}