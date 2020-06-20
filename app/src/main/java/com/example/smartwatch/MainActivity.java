package com.example.smartwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static Object lock = new Object();
    private Object lock2 = new Object();

    static TextView device1;
    Button reconnecta;
    Button disconnect;
    View v;
    Context con=null;
    ConnectThread conthread;
    private AlertDialog enableNotificationListenerAlertDialog;

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    //public static String incomingnumber,name;

//    public static void setname(String inc, String na) {
//        Log.d("mybt", "setname: called");
//        incomingnumber = inc;
//        name = na;
//        Log.d("mybt", "setname: end " +name + incomingnumber );
//
//    }
    public static void setDisText(){
        device1.setText("Disconnected During transition");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d("mybt", "onCreate: is called");

            device1 = (TextView) findViewById(R.id.devicename);
            reconnecta = (Button) findViewById(R.id.connect);
            disconnect = (Button) findViewById(R.id.disconnect);

        if(BackgroundTask.IN_BACK==0) {
            BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!btAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 2);
                //onCreate(savedInstanceState);

            }




            if (!isNotificationServiceEnabled()) {
                enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
                enableNotificationListenerAlertDialog.show();
            }




        /*if(!BluetoothAdapter.getDefaultAdapter().isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 2);
        }*/

            //Asking for permission for the app
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.READ_PHONE_STATE)) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                }
            } else {

            }
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.READ_CALL_LOG)) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_CALL_LOG}, 1);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_CALL_LOG}, 1);
                }
            } else {

            }

            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.READ_CONTACTS)) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS}, 1);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS}, 1);
                }
            } else {

            }

            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.WRITE_CONTACTS)) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_CONTACTS}, 1);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_CONTACTS}, 1);
                }
            } else {

            }
            //permission asking is over
            while(!btAdapter.isEnabled()){
                Log.d("mybt", "onCreate: inside the while loop");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            final BluetoothDevice device = btAdapter.getRemoteDevice("00:19:12:10:0A:EF");


            conthread = new ConnectThread(device);
            conthread.start();
            Log.d("mybt", "onCreate: is waiting ........................");
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d("mybt", "onCreate: is released*************************");
            if (!conthread.getStatus()) {
                device1.setText("Status : Not Connected");
                Log.d("mybt", "Status not connected");


            } else if (conthread.getStatus()) {
                device1.setText("Status : Connected");
                Log.d("mybt", "Status connected");

                BackgroundTask backgroundtask = new BackgroundTask(conthread);
                backgroundtask.execute();

            }


            reconnecta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Log.d("mybt", "onClick: for reconnect is called");
                    if (!conthread.getStatus()) {
                        Log.d("mybt", "onClick: for reconnect is called");
                        //new con.stop();
//                    ConnectThread t1 = new ConnectThread(device);
//                    t1.sta
                   /* device1.setText("Connecting.....");
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    /*if (!conthread.getStatus()) {
                        device1.setText("Status : Not Connected to hc05");
                        Log.d("mybt", "Status not connected");


                    } else if (conthread.getStatus()) {
                        device1.setText("Status : Connected to hc05");
                        Log.d("mybt", "Status connected");

                        BackgroundTask backgroundtask= new BackgroundTask(conthread);
                        backgroundtask.execute();


                    }*/
                    }
                }
            });
            disconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (conthread.getStatus()) {
                        conthread.cancel();
                        //conthread.getSocket().close();
                        //conthread.setStatus(false);

                        device1.setText("...Disconnected...");


                    }


                }
            });
        }else {
            device1.setText("CONNECTED reeeee au kete");


            disconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (conthread.getStatus()) {
                        conthread.cancel();
                        //conthread.getSocket().close();
                        //conthread.setStatus(false);

                        device1.setText("...Disconnected...");


                    }


                }
            });
        }
        Log.d("mybt", "onCreate: called ");



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_PHONE_STATE)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"permission Granted",Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(this,"permission Dined" ,Toast.LENGTH_SHORT).show();


                    }
                    return;
                }
        }
    }
    private AlertDialog buildNotificationServiceAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("notification_listener_service");
        alertDialogBuilder.setMessage("notification_listener_service_explanation");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton("no",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                    }
                });
        return(alertDialogBuilder.create());
    }

    private boolean isNotificationServiceEnabled(){
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d("testing", "onResume method called");
    }




}//Main class is over here




class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
   // private BluetoothSocket mmSocketFallback;
   public static BluetoothSocket mmSocketFallback;
    private static boolean SUCCESS = false;
    private static final String SERVICE_ID = "00001101-0000-1000-8000-00805f9b34fb"; //SPP UUID


    boolean getStatus(){
        if(mmSocketFallback.isConnected())
            return true;
        else
            return false;
    }

    void setStatus(boolean value){
        SUCCESS = value;
    }

    BluetoothSocket getSocket(){
        return mmSocketFallback;
    }


    ConnectThread(BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;

        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            // careful for attacks before connection is insecure
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(SERVICE_ID));


        } catch (IOException e) {
            Log.d("mybt", "Socket's create() method failed", e);
        }
        mmSocket = tmp;
        mmSocketFallback = tmp;
        //Log.d("mybt", "ConnectThread: jfdfdfjldf" + mmSocket.toString());
    }

    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
        //BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

       // mBluetoothAdapter.cancelDiscovery();
        BluetoothSocket tmp = null;
        tmp = mmSocket;
        /*try {
            Class<?> clazz = tmp.getRemoteDevice().getClass();
            Class<?>[] paramTypes = new Class<?>[]{Integer.TYPE};
            Method m = clazz.getMethod("createRfcommSocket", paramTypes);
            Object[] params = new Object[]{Integer.valueOf(1)};

            mmSocketFallback = (BluetoothSocket) m.invoke(tmp.getRemoteDevice(), params);
        } catch (Exception e) {
            Log.d("mybt", "Fall failed due to : " + e.getMessage());
        }*/
        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocketFallback.connect();
            SUCCESS = true;
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            Log.d("mybt", "Closing connection due to : " + connectException);
            try {
                mmSocketFallback.close();
                SUCCESS = false;


            } catch (IOException closeException) {
                Log.d("mybt", "Could not close the client socket due to : " + closeException, closeException);
            }
            Log.d("mybt", "Thread 2222222   "+mmSocketFallback.isConnected() );

        }


        synchronized (MainActivity.lock) {
            MainActivity.lock.notify();
        }
        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.


        //      manageMyConnectedSocket(mmSocket);
    }

    void cancel(){
        try {
            mmSocketFallback.close();
            SUCCESS=false;
        } catch (IOException e) {
            Log.d("mybt", "Could not close the client socket", e);
        }
    }
}
