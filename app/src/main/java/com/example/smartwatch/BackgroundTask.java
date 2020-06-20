package com.example.smartwatch;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.service.notification.NotificationListenerService;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class BackgroundTask extends AsyncTask<Void, Void, Void> {

//    String incomingno;
//    String name;
    public static int IN_BACK=0;
    Bundle save;


    //    TextView device_status = null;
//    TextView device_data=null;
    ConnectThread conthread = null;

//    void setcallername(String in, String n) {
//        incomingno = in;
//        name = n;
//    }

    public BackgroundTask() {

    }

// {
//        return new String[0];
//    }

    public BackgroundTask(ConnectThread connect) {
//        device_status= status;
//        device_data = data;
        conthread = connect;
    }

    @Override
    protected void onPreExecute() {
        Log.d("mybt", "onPreExecute: is called");

//        device_status.setText("Status : Connected");
//        device_data.setText("Data : Sending");

    }

    @Override
    protected void onPostExecute(Void result) {
        Log.d("mybt", "onPostExecute: is called");
//
//        device_status.setText("Status : Not Connected");
//        device_data.setText("Data : Not Sending");

    }

    @Override
    protected Void doInBackground(Void... strings) {
        IN_BACK=1;
        Log.d("mybt", "doInBackground: is called");
//        incomingno = strings[0];
//        name = strings[1];

        Log.d("mybt", "doInBackground: inside it name and number ");
        int i = 0;
        try {
            Thread.sleep(2000);
            Looper.prepare();
            MyBluetoothService.ConnectedThread connectedThread =
                    new MyBluetoothService.ConnectedThread(conthread.getSocket());

            while (true) {
                try {
                    Thread.sleep(1000);
//
//                    if(InterceptCall.IS_CALLING_STATE==0)
//                    {
//                        Log.d("mybt","Phone is ringing");
////                        InterceptCall.incomingNumber = null;
////                        InterceptCall.name= null;
//                    }

                    String hh1 = null,mm1,ss1,month1,date1;
                    byte[] byte_data = new byte[0];
                    //Retrieving current time
                    java.util.Calendar c = java.util.Calendar.getInstance();
                    int hh = c.get(java.util.Calendar.HOUR);
                    int mm = c.get(java.util.Calendar.MINUTE);
                    int ss = c.get(java.util.Calendar.SECOND);
                    int pm= c.get(java.util.Calendar.AM_PM);
                    int month = c.get(Calendar.MONTH);
                    int date = c.get(Calendar.DATE);
                    int year = c.get(Calendar.YEAR);
                    int day = c.get(Calendar.DAY_OF_WEEK);
                    if(hh==0 && pm==0)
                    {
                        hh=12;
                    }
                    if(hh==0 && pm==1){
                        hh=12;
                    }
                    if(hh>=0 && hh<10){
                        hh1 = "0"+hh;
                    }
                    else
                        hh1 = ""+hh;
                    if(mm>=0 && mm<10){
                        mm1 = "0"+mm;
                    }
                    else{
                        mm1 = ""+mm;
                    }

                    if(ss>=0 && ss<10){
                        ss1 = "0"+ss;
                    }
                    else{
                        ss1= ""+ss;
                    }

                    if(month>=0 && month<10){
                        month1 = "0"+month;
                    }
                    else
                        month1= ""+month;

                    if(date>=0 && date<10){
                        date1 = "0"+date;
                    }
                    else
                        date1=""+date;

                    Log.d("mybt","PM = "+pm);
                    Log.d("mybt","hh --" + hh);
                    Log.d("mybt","mm --" + mm);
                    Log.d("mybt","ss --" + ss);
                    Log.d("mybt","month = "+month);
                    Log.d("mybt","date = " + date);
                    Log.d("mybt","year = "+year);
                    Log.d("mybt","day = "+day);
//                    Log.d("mybt","hh --" + hh);
//                    Log.d("mybt","hh --" + hh);
//                    Log.d("mybt","hh --" + hh);

                    //Retrieving call information
//                    String incomingName=getContactName(con,INCOMING_NUMBER);
//                    //sending data to the watch
//                    String dataUrl = hh+":"+mm+":"+ss+":"+pm+"\0"+CALL_STATE_RING+"\0"+incomingName+"\0"+INCOMING_NUMBER+"\0"+MESSAGE_STATE+"\0"+INCOMING_MESSAGE_NUMBER+"\0"+INCOMING_MESSAGE_BODY+"\0";
//                    Log.d("mybt","Call state : "+CALL_STATE_RING+" Name : "+incomingName+" Num : "+INCOMING_NUMBER);
//                    Log.d("mybt","Message received :"+MESSAGE_STATE+" "+INCOMING_MESSAGE_NUMBER+" "+INCOMING_MESSAGE_BODY);

                    i++;
                    Log.d("mybt", "doInBackground: is sending data");
                    //String dataUrl = "call incoming from " + InterceptCall.name + i + " \n " + InterceptCall.incomingNumber + "notification--"
                    //+ NotificationListenerTesting.packName +NotificationListenerTesting.sendName+NotificationListenerTesting.detail+":";
                    String dataUrl = InterceptCall.IS_CALLING_STATE+":"+InterceptCall.name+"/"
                            +InterceptCall.incomingNumber+"|"+hh1+'!'+mm1+"@"+pm+"$"+month1+"%"+date1+"^"+day+"&"+NotificationListenerTesting.packName
                            +"*"+NotificationListenerTesting.sendName+"("+NotificationListenerTesting.detail+")"+NotificationListenerTesting.IS_NOTIFY+"_"+ InterceptCall.missed+"=";
                    Log.d("mybt", "doInBackground: on call " + dataUrl);

                    byte_data = dataUrl.getBytes();

                    connectedThread.write(byte_data);
                    InterceptCall.missed=0;
                    NotificationListenerTesting.detail=null;
                    NotificationListenerTesting.sendName=null;
                    NotificationListenerTesting.packName=null;
                    NotificationListenerTesting.IS_NOTIFY=0;
                    Thread.sleep(5000);
//                    if(MESSAGE_STATE==1)
//                    {
//                        MESSAGE_STATE=0;
//                        INCOMING_MESSAGE_NUMBER=null;
//                        INCOMING_MESSAGE_BODY=null;
//                    }
                } catch (InterruptedException e) {
                    Log.d("mybt", "doInBackground: exception:-"+e.toString());
                    e.printStackTrace();
                }
                if (!connectedThread.getWriteSocket().isConnected()) {
                    Log.d("mybt", "Disconnected during transmission");
                    MainActivity.setDisText();

                    break;
                }

            }
//            CALL_STATE_RING = 0;
//            MESSAGE_STATE=0;
//            INCOMING_NUMBER = null;
//            INCOMING_MESSAGE_BODY =null;
//            INCOMING_MESSAGE_NUMBER =null;


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }


}

