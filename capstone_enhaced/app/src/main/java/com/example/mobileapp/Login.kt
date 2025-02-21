//package com.example.mobileapp
//
//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//
//class Login : AppCompatActivity() {
//    var username: EditText? = null
//    var password: EditText? = null
//    var logIn: Button? = null
//    var toCA: TextView? = null
//    var db: databaseHelper? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//        username = findViewById(R.id.username_login)
//        password = findViewById(R.id.password_login)
//        logIn = findViewById(R.id.login_button)
//        toCA = findViewById(R.id.create_new_account)
//        db = databaseHelper()
//        logIn?.setOnClickListener(View.OnClickListener {
//            val User = ""
//            val user = username?.getText().toString()
//            val pass = password?.getText().toString()
//            if (user.isEmpty() or pass.isEmpty()) {
//                Toast.makeText(this@Login, "Empty username or password", Toast.LENGTH_LONG).show()
//            } else {
//                //Check if user exist
//                db?.checkUser(user, pass, object : databaseHelper.OnUserCheckListener {
//                    override fun onUserChecked(isValid: Boolean) {
//                        if (isValid) {
//                            //Check if user allows notification permission
//                            if (applicationContext.checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
//                                val intent3 = Intent(this@Login, events::class.java)
//                                this@Login.startActivity(intent3)
//                                Toast.makeText(this@Login, "Login Successful", Toast.LENGTH_LONG).show()
//                            } else {
//                                val askPerm = Intent(this@Login, allowNotification::class.java)
//                                this@Login.startActivity(askPerm)
//                            }
//                        } else {
//                            Toast.makeText(this@Login, "Account doesn't Exists", Toast.LENGTH_LONG).show()
//                        }
//                    }
//                })
//
//            }
//        })
//        toCA?.setOnClickListener(View.OnClickListener {
//            val create_account_page = Intent(this@Login, createAccount::class.java)
//            this@Login.startActivity(create_account_page)
//        })
//    }
//}


package com.example.mobileapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class Login : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var logIn: Button
    private lateinit var toCA: TextView
    private lateinit var db: databaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.username_login)
        password = findViewById(R.id.password_login)
        logIn = findViewById(R.id.login_button)
        toCA = findViewById(R.id.create_new_account)

        db = databaseHelper() // Ensure correct context is passed

        logIn.setOnClickListener {
            val user = username.text.toString().trim()
            val pass = password.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Empty username or password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            db.checkUser(user, pass, object : databaseHelper.OnUserCheckListener {
                override fun onUserChecked(isValid: Boolean) {
                    if (isValid) {
                        // Check if SMS permission is granted
                        if (ContextCompat.checkSelfPermission(this@Login, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                            startActivity(Intent(this@Login, events::class.java))
                            Toast.makeText(this@Login, "Login Successful", Toast.LENGTH_LONG).show()
                        } else {
                            startActivity(Intent(this@Login, allowNotification::class.java))
                        }
                    } else {
                        Toast.makeText(this@Login, "Account doesn't exist", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }

        toCA.setOnClickListener {
            startActivity(Intent(this, createAccount::class.java))
        }
    }
}
