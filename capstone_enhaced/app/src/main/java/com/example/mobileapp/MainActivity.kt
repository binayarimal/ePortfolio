package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val database = Firebase.database
        val myRef = database.getReference("message")


        Log.d("Firebase", "Writing value: Hello, World!")
        myRef.setValue("Hello, People!")
            .addOnSuccessListener { Log.d("Firebase", "Data written successfully!") }
            .addOnFailureListener { e -> Log.w("Firebase", "Failed to write data.", e) }

        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val intent = Intent(this@MainActivity, Login::class.java)
        this@MainActivity.startActivity(intent)

        // Write a message to the database

    }
}