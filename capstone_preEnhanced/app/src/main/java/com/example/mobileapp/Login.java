package com.example.mobileapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {
    EditText username, password;
    Button logIn;
    TextView toCA;
    databaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username_login);
        password = findViewById(R.id.password_login);
        logIn = findViewById(R.id.login_button);
        toCA = findViewById(R.id.create_new_account);
        db = new databaseHelper(this);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String User = "";
                String user = username.getText().toString();
                String pass = password.getText().toString();
                if(user.isEmpty() | pass.isEmpty()){
                    Toast.makeText(Login.this, "Empty username or password", Toast.LENGTH_LONG).show();
                }
                else{
                    //Check if user exist
                    boolean checkUser = db.checkUser( user, pass);
                    if(checkUser) {

                        //Check if user allows notification permission
                        if (getApplicationContext().checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                            Intent intent3 = new Intent(Login.this, events.class);
                            Login.this.startActivity(intent3);

                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();
                        } else{
                            Intent askPerm = new Intent(Login.this, allowNotification.class);
                            Login.this.startActivity(askPerm);
                        }

                    }else{
                        Toast.makeText(Login.this, "Account doesn't Exists", Toast.LENGTH_LONG).show();

                    }
                }



            }
        });

        toCA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent create_account_page= new Intent(Login.this, createAccount.class);
                Login.this.startActivity(create_account_page);

            }
        });

    }
}