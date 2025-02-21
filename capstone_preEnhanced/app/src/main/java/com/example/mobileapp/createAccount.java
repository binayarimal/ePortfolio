package com.example.mobileapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;


public class createAccount extends AppCompatActivity {

    EditText username, password, passwordRetype;

    Button signUp;

    TextView logInDirect;
    databaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        username = findViewById(R.id.username_input);
        password = findViewById(R.id.password_input);
        passwordRetype = findViewById(R.id.password_retype);
        signUp = findViewById(R.id.create_account);
        logInDirect = findViewById(R.id.login_screen);
        db = new databaseHelper(this);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String passRetype = passwordRetype.getText().toString();

                System.out.println("hello");
                System.out.println(user);

                if(user.isEmpty() | pass.isEmpty()){
                    Toast.makeText(createAccount.this, "Empty username or password", Toast.LENGTH_LONG).show();
                }
                else if(!passRetype.equals(pass)){
                    Toast.makeText(createAccount.this, "Passwords do not match", Toast.LENGTH_LONG).show();

                }
                else{
                    boolean checkUser = db.checkUser( user, pass); //Check is user already exists
                    if(checkUser) {
                        Toast.makeText(createAccount.this, "Account Already Exists", Toast.LENGTH_LONG).show();
                    }else{
                        boolean insertUser = db.insertUserData(user, pass); //if user doesn't exist add user
                        if (!insertUser){
                            Toast.makeText(createAccount.this, "Internal Error", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(createAccount.this, "Account Created Successfully", Toast.LENGTH_LONG).show();
                            Intent toLogIn = new Intent(createAccount.this, Login.class);
                            createAccount.this.startActivity(toLogIn);

                        }
                    }
                }

            }
        });

        logInDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(createAccount.this, Login.class);
                createAccount.this.startActivity(intent2);

            }
        });



    }
}