package com.example.smartwatch;//package com.example.smartwatch;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.smartwatch.MainActivity;

public class InterceptCall extends BroadcastReceiver {
    public static  String name;
    public static  String incomingNumber ;
    public static int IS_CALLING_STATE = 0;
    private boolean ring;
    private boolean callReceived;
    static int missed=0;

    @Override
    public void onReceive(Context context, Intent intent) {

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String msg = "Phone state changed to " + state;
        Toast.makeText(context,"call state changed to "+ state,Toast.LENGTH_SHORT).show();





        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING))
        {
            ring =true;
        }
        Log.d("call", "onReceive: ring "+ ring);


        // If incoming call is received
        if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
        {
            callReceived=true;
        }
        Log.d("call", "onReceive: offhook "+ callReceived);


        // If phone is Idle
        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE))
        {
            // If phone was ringing(ring=true) and not received(callReceived=false) , then it is a missed call
            if(ring==false&&callReceived==false)
            {
                missed=1;
                Toast.makeText(context, "It was A MISSED CALL from : "+incomingNumber, Toast.LENGTH_LONG).show();
            }
        }
        Log.d("call", "onReceive: state "+ state);

        if(state.equals("IDLE") || state.equals("OFFHOOK")) {
            name = null;
            incomingNumber = null;
            IS_CALLING_STATE = 0;
        }


        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {                                   // 4
            incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);  // 5
            IS_CALLING_STATE =1;
            name = getContactName(context, incomingNumber);
            msg += ". Incoming number is  " + incomingNumber;
            ring =true;

            // TODO This would be a good place to "Do something when the phone rings" ;-)

        }
        try{
            Toast.makeText(context, msg+" "+name +" from smartwatch", Toast.LENGTH_LONG).show();
        }
        catch(Exception e){
            e.printStackTrace();
        }



    }
//    public String getinfo(){
//        return name+"calling\n"+incomingNumber;
//    }

    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = "Unknown";
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(!cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }



}