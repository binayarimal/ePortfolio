//package com.example.mobileapp
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.mobileapp.databaseHelper.OnUserInsertListener
//
//class createAccount : AppCompatActivity() {
//    var username: EditText? = null
//    var password: EditText? = null
//    var passwordRetype: EditText? = null
//    var signUp: Button? = null
//    var logInDirect: TextView? = null
//    var db: databaseHelper? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_create_account)
//        username = findViewById(R.id.username_input)
//        password = findViewById(R.id.password_input)
//        passwordRetype = findViewById(R.id.password_retype)
//        signUp = findViewById(R.id.create_account)
//        logInDirect = findViewById(R.id.login_screen)
//        db = databaseHelper()
//        signUp?.setOnClickListener(View.OnClickListener {
//            val user = username?.getText().toString()
//            val pass = password?.getText().toString()
//            val passRetype = passwordRetype?.getText().toString()
//            println("hello")
//            println(user)
//            if (user.isEmpty() or pass.isEmpty()) {
//                Toast.makeText(this@createAccount, "Empty username or password", Toast.LENGTH_LONG)
//                    .show()
//            } else if (passRetype != pass) {
//                Toast.makeText(this@createAccount, "Passwords do not match", Toast.LENGTH_LONG)
//                    .show()
//            } else {
//
//                db?.checkUser(user, pass, object : databaseHelper.OnUserCheckListener {
//                    override fun onUserChecked(isValid: Boolean) {
//                        if (isValid) {
//                            Toast.makeText(this@createAccount, "Account Already Exists", Toast.LENGTH_LONG)
//                                .show()
//                        } else {
//
//                            db?.insertUserData(
//                                user,
//                                pass,
//                                object : OnUserInsertListener {
//                                    override fun onSuccess(userId: String) {
//                                        Toast.makeText(
//                                            applicationContext,
//                                            "User added! ID: $userId",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//
//                                    override fun onFailure(errorMessage: String) {
//                                        Toast.makeText(
//                                            applicationContext,
//                                            "Failed to add user: $errorMessage",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                                })
//                        }
//                    }
//                })
//
//            }
//        })
//        logInDirect?.setOnClickListener(View.OnClickListener {
//            val intent2 = Intent(this@createAccount, Login::class.java)
//            this@createAccount.startActivity(intent2)
//        })
//    }
//}

package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapp.databaseHelper.OnUserInsertListener

class createAccount : AppCompatActivity() {
    private var username: EditText? = null
    private var password: EditText? = null
    private var passwordRetype: EditText? = null
    private var signUp: Button? = null
    private var logInDirect: TextView? = null
    private var db: databaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        username = findViewById(R.id.username_input)
        password = findViewById(R.id.password_input)
        passwordRetype = findViewById(R.id.password_retype)
        signUp = findViewById(R.id.create_account)
        logInDirect = findViewById(R.id.login_screen)
        db = databaseHelper()

        signUp?.setOnClickListener {
            val user = username?.text.toString()
            val pass = password?.text.toString()
            val passRetype = passwordRetype?.text.toString()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Empty username or password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (pass != passRetype) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            db?.checkUser(user, pass, object : databaseHelper.OnUserCheckListener {
                override fun onUserChecked(isValid: Boolean) {
                    if (isValid) {
                        Toast.makeText(this@createAccount, "Account Already Exists", Toast.LENGTH_LONG).show()
                    } else {
                        db?.insertUserData(user, pass, object : OnUserInsertListener {
                            override fun onSuccess(userId: String) {
                                Toast.makeText(applicationContext, "Account Created! Redirecting...", Toast.LENGTH_SHORT).show()
                                // Redirect to Login or Events Page
                                val intent = Intent(this@createAccount, Login::class.java)
                                startActivity(intent)
                                finish() // Finish this activity so the user doesn't come back when pressing "back"
                            }

                            override fun onFailure(errorMessage: String) {
                                Toast.makeText(applicationContext, "Failed to add user: $errorMessage", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            })
        }

        logInDirect?.setOnClickListener {
            val intent = Intent(this@createAccount, Login::class.java)
            startActivity(intent)
        }
    }
}
