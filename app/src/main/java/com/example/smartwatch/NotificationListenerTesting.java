package com.example.smartwatch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.RequiresApi;

//@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class NotificationListenerTesting extends NotificationListenerService {
    public static int IS_NOTIFY=0;
    public static String packName;
    public static String sendName;
    public static String detail;

    public String TAG = "mybt";
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.d(TAG, "onNotificationPosted: is called");
        String pack = sbn.getPackageName();
        Log.d("test", "PACKAGENAME - " + pack);

        String ticker ="";
        if(sbn.getNotification().tickerText !=null) {
            ticker = sbn.getNotification().tickerText.toString();
        }
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = null;
        if (extras.getCharSequence("android.text") != null) {//geting the main text inside the notification
            text = extras.getCharSequence("android.text").toString();
        }



        Object data = extras.get("android.bigText");
        if (data == null) {
            data =extras.get("android.text");
        }
        if(data != null){
            Log.d("test", "BigText" + data.toString());
        }
        else{
            Log.d("test", "BigText is not found");
        }

        Log.d("test", "ticker" + ticker);



        if(text == null){
            if (extras.get("android.textLines") != null) {
                CharSequence[] charText = (CharSequence[]) extras
                        .get("android.textLines");
                if (charText.length > 0) {
                    text = charText[charText.length - 1].toString();
                }
            }
        }
        Log.d("test", "TEXT of "+pack+" - " + text);
        Log.d("test", "TEXT  id of "+pack+" - " +sbn.getId() );


        if(pack.equals("com.whatsapp")){
            IS_NOTIFY=1;
            Log.d("test", "onNotificationPosted: notification found" + pack);
            Log.d("test", "TEXT" + text);
            if(data != null){
                Log.d("test", "BigText" + data.toString());
            }
            else{
                Log.d("test", "BigText is not found");
            }
            Log.d("test", "Title" + extras.getString("android.title"));
            Log.d("test", "ticker" + ticker);
            packName = "WhatsApp";
            sendName = title;
            detail = data.toString();



        }



//        if(pack.equals("com.jio.media.jiobeats")){
//            Log.d("test", "onNotificationPosted: notification found" + pack);
//            Log.d("test", "TEXT" + text);
//            if(data != null){
//                Log.d("test", "BigText" + data.toString());
//            }
//            else{
//                Log.d("test", "BigText is not found");
//            }
//            Log.d("test", "Title" + extras.getString("android.title"));
//            Log.d("test", "ticker" + ticker);
//            packName = "JioSavan";
//            sendName = title;
//            detail = data.toString();
//
//
//
//        }



        if(pack.equals("com.android.server.telecom")){
            IS_NOTIFY=1;
            Log.d("test", "onNotificationPosted: notification found" + pack);
            Log.d("test", "TEXT" + text);
            if(data != null){
                Log.d("test", "BigText" + data.toString());
            }
            else{
                Log.d("test", "BigText is not found");
            }
            Log.d("test", "Title" + extras.getString("android.title"));
            Log.d("test", "ticker" + ticker);
            packName = "MissedCall";
            sendName = title;
            detail = data.toString();



        }



        if(pack.equals("com.google.android.apps.messaging")){
            IS_NOTIFY=1;
            Log.d("test", "onNotificationPosted: notification found" + pack);
            Log.d("test", "TEXT" + text);
            if(data != null){
                Log.d("test", "BigText" + data.toString());
            }
            else{
                Log.d("test", "BigText is not found");
            }
            Log.d("test", "Title" + extras.getString("android.title"));
            Log.d("test", "ticker" + ticker);
            packName = "Message";
            sendName = title;
            detail = data.toString();



        }


        if(pack.equals("com.google.android.gm")){
            IS_NOTIFY=1;
            Log.d("test", "onNotificationPosted: notification found" + pack);
            Log.d("test", "TEXT" + text);
            if(data != null){
                Log.d("test", "BigText" + data.toString());
            }
            else{
                Log.d("test", "BigText is not found");
            }
            Log.d("test", "Title" + extras.getString("android.title"));
            Log.d("test", "ticker" + ticker);
            packName = "Gmail";
            sendName = title;
            detail = data.toString();



        }




    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.d("test", "onNotificationPosted: notification removed");
        //Log.d("test", "onNotificationPosted: notification removed" + pack);
    }
}
