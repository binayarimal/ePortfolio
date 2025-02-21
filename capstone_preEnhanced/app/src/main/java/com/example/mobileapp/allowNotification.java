package com.example.mobileapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.telephony.SmsManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class allowNotification extends AppCompatActivity {
    Button send, deny;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_allow_notification);

        send=findViewById(R.id.allow);
        String phoneNumber="5551234567";
        String message="You will recieve SMS from event tracker.";


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Requesting permission from user to send messages
                requestPermissions(new String[]{android.Manifest.permission.SEND_SMS}, 1);

                //Send user message if permission is granted

                if (getApplicationContext().checkSelfPermission(android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                    try {
                        SmsManager smsManager=SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber,null,message,null,null);
                        Toast.makeText(getApplicationContext(),"Message Sent",Toast.LENGTH_LONG).show();
                    }catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                    Intent eventScreen = new Intent(allowNotification.this, events.class);
                    allowNotification.this.startActivity(eventScreen);
            }   }
        });


        //Proceed without sending message if user doesn't grant permission
        deny = findViewById(R.id.dontallow);
        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eventScreen = new Intent(allowNotification.this, events.class);
                allowNotification.this.startActivity(eventScreen);
            }
        });


    }


}