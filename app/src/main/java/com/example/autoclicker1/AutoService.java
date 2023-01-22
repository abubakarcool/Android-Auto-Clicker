package com.example.autoclicker1;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.os.HandlerThread;
import android.util.Log;
import android.graphics.Path;
import android.view.accessibility.AccessibilityEvent;
import android.os.Handler;
import android.widget.Toast;

//privileged service that helps users process information on the screen and lets them to interact meaningfully with a device
public class AutoService extends AccessibilityService {
    private Handler mHandler;
    private IntervalRunnable mRunnable;//our own created class object
    private int mX;
    private int mY;
    @Override
    public void onCreate() {
        super.onCreate();
        //sending tasks to the main/ui thread, primarily to update the UI from any other thread.
        HandlerThread mHandlerThread =new HandlerThread("auto-handler");//a thread that has a looper
        mHandlerThread.start();
        mHandler=new Handler(mHandlerThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {//is called every time a client starts the service using startService(Intent intent)
        Log.d("autoService","started");
        if(intent!=null){
            String action = intent.getStringExtra("action");//getting the previous activity msg with tag action
            if(action.equals("play")){
                mX=intent.getIntExtra("x",0);
                mY=intent.getIntExtra("y",0);
                if (mRunnable==null){
                    mRunnable=new IntervalRunnable();
                }
                mHandler.post(mRunnable);//whenever you want to do operations on the UI thread
                Toast.makeText(getBaseContext(),"started",Toast.LENGTH_SHORT).show();
            }
            else if(action.equals("stop")){
                mHandler.removeCallbacksAndMessages(null);//only remove messages and callbacks that have been posted to that specific Handler
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onServiceConnected() { }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) { }

    @Override
    public void onInterrupt() { }

    private void playTap(int x, int y){
        Toast.makeText(getBaseContext(),"in PlayTap",Toast.LENGTH_SHORT).show();
        Path swipePath=new Path();
        swipePath.moveTo(x,y);//It sets the starting point of your line
        swipePath.lineTo(x,y);//Add a line from the last point to the specified point (x,y)
        GestureDescription.Builder gestbuildr=new GestureDescription.Builder();
        gestbuildr.addStroke(new GestureDescription.StrokeDescription(swipePath,0,200));
        dispatchGesture(gestbuildr.build(), new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                mHandler.post(mRunnable);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
            }
        },null);

    }


    //Runnable is an interface that is to be implemented by a class whose instances are intended to be executed by a thread
    private class IntervalRunnable implements Runnable{
        @Override
        public void run() {
            Toast.makeText(getBaseContext(),"in run now",Toast.LENGTH_SHORT).show();
            playTap(mX,mY);
        }
    }
}
