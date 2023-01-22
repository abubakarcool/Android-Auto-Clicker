package com.example.autoclicker1;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;

public class FloatingView extends Service implements View.OnClickListener {
    private WindowManager mWindowManager;
    private WindowManager mWindowManager2;
    private View myFloatingView;
    private View myFloatingView2;
    @Override
    public void onCreate() {
        super.onCreate();
        ////getting the widget layout from xml using layout inflater ...dynamic layout use inflater
        myFloatingView= LayoutInflater.from(this).inflate(R.layout.floating_view,null);
        myFloatingView2=LayoutInflater.from(this).inflate(R.layout.pointer_view,null);
        int layout_type;//getting the type of layout parameter
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            ///Application overlay windows are displayed above all activity windows but below critical system windows like the status bar or IME
            layout_type=WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        else{
            //These windows are normally placed above all applications, but behind the status bar
            layout_type=WindowManager.LayoutParams.TYPE_PHONE;
        }
        final WindowManager.LayoutParams parmss=  new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,//width
                WindowManager.LayoutParams.WRAP_CONTENT,//height
                layout_type,//type
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,//flags
                PixelFormat.TRANSLUCENT//format
        );
        final WindowManager.LayoutParams parmss2= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layout_type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
        );
        //specify the view position
        parmss.gravity= Gravity.CENTER | Gravity.LEFT;
        parmss.x=5;
        parmss.y=50;
        parmss2.gravity=Gravity.CENTER | Gravity.CENTER;
        //getting windows services and adding the floating view to it
        mWindowManager=(WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager2=(WindowManager)getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(myFloatingView,parmss);
        mWindowManager2.addView(myFloatingView2,parmss2);
        myFloatingView.findViewById(R.id.thisIsAnID).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //remember the initial position.
                        initialX=parmss.x;
                        initialY= parmss.y;
                        //get the touch location
                        initialTouchX=event.getRawX();
                        initialTouchY=event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float Xdiff= Math.round(event.getRawX()-initialTouchX);
                        float Ydiff= Math.round(event.getRawY()-initialTouchY);
                        //Calculate the X and Y coordinates of the view.
                        parmss.x=initialX+(int)Xdiff;
                        parmss.y=initialY+(int)Ydiff;
                        //Update the layout with new X & Y coordinates
                        mWindowManager.updateViewLayout(myFloatingView,parmss);
                        return true;
                }
                return false;
            }
        });
        myFloatingView2.findViewById(R.id.pointerr).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //remember the initial position.
                        initialX=parmss2.x;
                        initialY= parmss2.y;
                        //get the touch location
                        initialTouchX=event.getRawX();
                        initialTouchY=event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float Xdiff= Math.round(event.getRawX()-initialTouchX);
                        float Ydiff= Math.round(event.getRawY()-initialTouchY);
                        //Calculate the X and Y coordinates of the view.
                        parmss2.x=initialX+(int)Xdiff;
                        parmss2.y=initialY+(int)Ydiff;
                        //Update the layout with new X & Y coordinates
                        mWindowManager2.updateViewLayout(myFloatingView2,parmss2);
                        return true;
                }
                return false;
            }
        });
        ToggleButton tglbtn1=(ToggleButton) myFloatingView.findViewById(R.id.toggl);
        tglbtn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            Intent inn2=new Intent(getApplicationContext(),AutoService.class);
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    int []loc=new int[2];//array of two int objects
                    myFloatingView2.getLocationOnScreen(loc);//get x & y values from floating view to array
                    inn2.putExtra("action","play");
                    inn2.putExtra("x",loc[0]-1);
                    inn2.putExtra("y",loc[1]-1);
                    Toast play_pressed=Toast.makeText(getApplicationContext(),"x: "+String.valueOf(loc[0]-1)+", y: "+String.valueOf(loc[1]-1),Toast.LENGTH_SHORT);
                    play_pressed.show();
                }else{
                    inn2.putExtra("action","stop");
                    Toast pasuse_pressed = Toast.makeText(getApplicationContext(), "pause pressed", Toast.LENGTH_SHORT);
                    pasuse_pressed.show();
                }
            getApplication().startService(inn2);
            }
        });
//        Button startbtn=(Button) myFloatingView.findViewById(R.id.start);
//        startbtn.setOnClickListener(this);
        Button stopbtnclose =(Button) myFloatingView.findViewById(R.id.stop);
        stopbtnclose.setOnClickListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onClick(View v) {
        super.onDestroy();
        if(myFloatingView!=null | myFloatingView2!=null){
            mWindowManager.removeView(myFloatingView);
            mWindowManager2.removeView(myFloatingView2);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(myFloatingView!=null | myFloatingView2!=null){
            mWindowManager.removeView(myFloatingView);
            mWindowManager2.removeView(myFloatingView2);
        }
    }
}
